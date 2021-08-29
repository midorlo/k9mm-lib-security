package com.midorlo.k9.components.security;

import com.midorlo.k9.repository.security.AuthorityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

/**
 * <p>Custom Authorization Manager Implementation.</p>
 * <p>References a tree description of the api.</p>
 * <pre>
 * /
 * └── /security
 *     └── /login
 *        ├── Authorization(GET, /security/login)
 *        └── ...
 * </pre>
 */
@Slf4j
@Component
public class AuthorizationManagerImpl implements AuthorizationManager<HttpServletRequest> {

    private final AuthorityRepository authorityRepository;

    public AuthorizationManagerImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, HttpServletRequest object) {
        log.info("verify({},{})", authentication, object);
        AuthorizationDecision decision = check(authentication, object);
        if (decision != null && !decision.isGranted()) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> supplier, HttpServletRequest request) {
        log.info("check({}, {})", supplier, request);
        Authentication authentication = resolveSupplier(supplier);
        boolean result = authorityRepository.findByEndpoint_ServletPathEqualsIgnoreCaseAndMethodEquals(
                        request.getServletPath(),
                        HttpMethod.valueOf(request.getMethod()))
                .map(authority -> authentication != null && authentication.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .anyMatch(s -> s.equals(authority.getAuthority())))
                .orElse(true);
        return new AuthorizationDecision(result);
    }

    /**
     * Wraps the call of the authentication supplier.
     *
     * @param authenticationSupplier authenticationSupplier.
     * @return authenticationSupplier or null.
     */
    private Authentication resolveSupplier(Supplier<Authentication> authenticationSupplier) {
        Authentication authentication = null;
        try {
            authentication = authenticationSupplier.get();
        } catch (Exception ignored) {
        }
        return authentication;
    }
}
