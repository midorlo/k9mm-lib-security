package com.midorlo.k9.components.security;

import com.midorlo.k9.model.security.GrantedAuthorityImpl;
import com.midorlo.k9.model.security.mapper.AuthorityInfoMapper;
import com.midorlo.k9.repository.security.AuthorityRepository;
import hu.webarticum.treeprinter.ListingTreePrinter;
import hu.webarticum.treeprinter.SimpleTreeNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>Custom Authorization Manager Implementation.</p>
 * <p>References a tree description of the api.</p>
 * <pre>
 *
 * Map<RestMetadata, Map<HttpMethod, Authority>
 * └── /security
 *     └──├── /login            Map<HttpMethod,Authority[]>
 *        ├── /profile          Map<HttpMethod,Authority[]>
 *        └── /profile/batman   Map<HttpMethod,Authority[]>
 * </pre>
 */
@Slf4j
@Component
public class AuthorizationManagerImpl implements AuthorizationManager<HttpServletRequest>,
                                                 ApplicationListener<ContextRefreshedEvent> {

    private Map<String, Map<HttpMethod, Long>> requirements;
    private final AuthorityRepository authorityRepository;

    /**
     * @implNote since the restrictions map rarely changes within runtime, its more efficient to just keep them all
     * in memory permanently.
     */
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
        boolean result =
                authorityRepository.findByServletPathPathEqualsIgnoreCaseAndMethodEquals(request.getServletPath(),
                                                                                         HttpMethod.valueOf(request.getMethod()))
                                   .map(authority -> authentication != null
                                                     && authentication.getAuthorities()
                                                                      .stream()
                                                                      .map(GrantedAuthority::getAuthority)
                                                                      .anyMatch(s -> s.equals(new GrantedAuthorityImpl(authority).getAuthority())))
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

    private void logCurrentAuthorizationTree() {
        this.requirements = new AuthorityInfoMapper().toTree(authorityRepository.findAll());
        SimpleTreeNode treeRoot = new SimpleTreeNode("/");
        requirements.keySet()
                    .stream()
                    .map(logKey -> {
                        SimpleTreeNode        treeLog = new SimpleTreeNode(logKey);
                        Map<HttpMethod, Long> twigMap = requirements.get(logKey);

                        twigMap.keySet().stream()
                               .map(twigKey -> {
                                   SimpleTreeNode twig =
                                           new SimpleTreeNode(twigKey.name());
                                   SimpleTreeNode leaf =
                                           new SimpleTreeNode(twigMap.get(twigKey)
                                                                     .toString());
                                   twig.addChild(leaf);
                                   return twig;
                               })
                               .forEach(treeLog::addChild);
                        return treeLog;
                    }).forEach(treeRoot::addChild);
        log.info(
                "Parsed the following authorization model:"
                + System.lineSeparator()
                + System.lineSeparator()
                +
                new ListingTreePrinter().getAsString(treeRoot)
                + System.lineSeparator()
                + System.lineSeparator()
                );
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
        logCurrentAuthorizationTree();
    }
}
