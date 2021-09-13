package com.midorlo.k9.web.advice.security;

import com.midorlo.k9.configuration.security.ErrorConstants;
import com.midorlo.k9.exception.security.UsernameAlreadyUsedException;
import com.midorlo.k9.web.exception.*;
import com.midorlo.k9.web.util.HeaderUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.*;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;
import org.zalando.problem.violations.ConstraintViolationProblem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
public class SecurityExceptionControllerAdvice implements ProblemHandling,
                                                          SecurityAdviceTrait {

    static final String[] BASE_PACKAGES = { "org.", "java.", "net.", "javax.", "com.", "io.", "de.", "com.midorlo.k9" };

    private static final String FIELD_ERRORS_KEY = "fieldErrors";
    private static final String MESSAGE_KEY      = "message";
    private static final String PATH_KEY         = "path";
    private static final String VIOLATIONS_KEY   = "violations";

    private final Collection<String> activeProfiles;

    @Value("${k9.fqdn}")
    private String applicationName;

    private final Environment env;

    public SecurityExceptionControllerAdvice(Environment env) {
        this.env            = env;
        this.activeProfiles = Arrays.asList(env.getActiveProfiles());
    }

    /**
     * Post-process the Problem payload to add the message key for the front-end if needed.
     */
    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity,
                                           @NonNull NativeWebRequest request) {
        if (entity == null) {
            return null;
        }
        Problem problem = entity.getBody();
        if (!(problem instanceof ConstraintViolationProblem || problem instanceof DefaultProblem)) {
            return entity;
        }

        HttpServletRequest nativeRequest = request.getNativeRequest(HttpServletRequest.class);
        String             requestUri    = nativeRequest != null ? nativeRequest.getRequestURI() : StringUtils.EMPTY;
        ProblemBuilder builder = Problem
                .builder()
                .withType(Problem.DEFAULT_TYPE.equals(problem.getType()) ? ErrorConstants.DEFAULT_TYPE :
                          problem.getType())
                .withStatus(problem.getStatus())
                .withTitle(problem.getTitle())
                .with(PATH_KEY, requestUri);

        if (problem instanceof ConstraintViolationProblem) {
            builder
                    .with(VIOLATIONS_KEY, ((ConstraintViolationProblem) problem).getViolations())
                    .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION);
        } else {
            builder.withCause(((DefaultProblem) problem).getCause()).withDetail(problem.getDetail())
                   .withInstance(problem.getInstance());
            problem.getParameters().forEach(builder::with);
            if (!problem.getParameters().containsKey(MESSAGE_KEY) && problem.getStatus() != null) {
                builder.with(MESSAGE_KEY, "error.http." + problem.getStatus().getStatusCode());
            }
        }
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), entity.getStatusCode());
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldErrorVM> fieldErrors = result
                .getFieldErrors()
                .stream()
                .map(
                        f ->
                                new FieldErrorVM(
                                        f.getObjectName().replaceFirst("DTO$", ""),
                                        f.getField(),
                                        StringUtils.isNotBlank(f.getDefaultMessage()) ? f.getDefaultMessage() :
                                        f.getCode()
                                )
                    )
                .collect(Collectors.toList());

        Problem problem = Problem
                .builder()
                .withType(ErrorConstants.CONSTRAINT_VIOLATION_TYPE)
                .withTitle("Method argument not valid")
                .withStatus(defaultConstraintViolationStatus())
                .with(MESSAGE_KEY, ErrorConstants.ERR_VALIDATION)
                .with(FIELD_ERRORS_KEY, fieldErrors)
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex,
                                                                   NativeWebRequest request) {
        EmailAlreadyUsedException problem = new EmailAlreadyUsedException();
        return create(problem,
                      request,
                      HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(),
                                                    problem.getErrorKey(),
                                                    problem.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleUsernameAlreadyUsedException(UsernameAlreadyUsedException ex,
                                                                      NativeWebRequest request) {
        LoginAlreadyUsedException problem = new LoginAlreadyUsedException();
        return create(problem,
                      request,
                      HeaderUtil.createFailureAlert(applicationName, true, problem.getEntityName(),
                                                    problem.getErrorKey(),
                                                    problem.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleInvalidPasswordException(InvalidPasswordException ex,
                                                                  NativeWebRequest request) {
        return create(new InvalidPasswordException(), request);
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleBadRequestAlertException(BadRequestAlertException ex,
                                                                  NativeWebRequest request) {
        return create(ex,
                      request,
                      HeaderUtil.createFailureAlert(applicationName,
                                                    true,
                                                    ex.getEntityName(),
                                                    ex.getErrorKey(),
                                                    ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleConcurrencyFailure(ConcurrencyFailureException ex,
                                                            NativeWebRequest request) {
        return create(ex,
                      Problem.builder()
                             .withStatus(Status.CONFLICT)
                             .with(MESSAGE_KEY, ErrorConstants.ERR_CONCURRENCY_FAILURE)
                             .build(),
                      request);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ProblemBuilder prepare(final Throwable throwable,
                                  final StatusType status,
                                  final URI type) {

        String userMessage = throwable.getMessage();

        if (activeProfiles.contains("prd")) {
            if (throwable instanceof HttpMessageConversionException) {
                userMessage = "Unable to convert http message";
            }
            if (throwable instanceof DataAccessException) {
                userMessage = "Failure during data access";
            }
            if (StringUtils.containsAny(throwable.getMessage(), BASE_PACKAGES)) {
                userMessage = "Unexpected runtime exception";
            }
        }

        return Problem
                .builder()
                .withType(type)
                .withTitle(status.getReasonPhrase())
                .withStatus(status)
                .withDetail(userMessage)
                .withCause(Optional.ofNullable(throwable.getCause()).filter(cause -> isCausalChainsEnabled())
                                   .map(this::toProblem).orElse(null));
    }
}
