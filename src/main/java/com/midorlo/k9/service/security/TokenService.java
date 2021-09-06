package com.midorlo.k9.service.security;

import com.midorlo.k9.configuration.security.SecurityProperties;
import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.GrantedAuthorityImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.midorlo.k9.util.security.DateUtilities.*;

@Slf4j
@Service
public class TokenService {

    private final SecurityProperties securityProperties;
    private final Key encryptionKey;
    private final JwtParser tokenParser;

    public TokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.encryptionKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.securityProperties.getKey()));
        this.tokenParser = Jwts.parserBuilder().setSigningKey(encryptionKey).build();
    }

    /**
     * Gets the JSON Web Token for the current principal.
     *
     * @return the JSON Web Token.
     */
    public Optional<String> getPrincipalToken() {
        log.info("getPrincipalToken()");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Creates a new token for given <b>successfully authenticated</b> account.
     */
    public String createNewAuthenticationToken(Account account, boolean doExtendExpirationDate) {
        log.info("createNewAuthenticationToken({},{})", account, doExtendExpirationDate);

        String authorities = account.getAuthorities()
                .stream()
                .map(GrantedAuthorityImpl::new)
                .map(GrantedAuthorityImpl::getAuthority)
                .collect(Collectors.joining(","));

        String token = Jwts.builder()
                .setSubject(String.format("%s,%s", account.getId(), account.getEmail()))
                .setIssuer(securityProperties.getIssuer())
                .setIssuedAt(now())
                .setExpiration(doExtendExpirationDate ? inOneMonth() : inOneWeek())
                .signWith(encryptionKey)
                .compact();
        log.info("Created token {}", token);
        return token;
    }

    /**
     * Determines if the given json web token is currently registered for any authorization.
     *
     * @param authToken scoped json web token.
     * @return true, if the given json web token is currently registered for any authorization.
     */
    public boolean isAuthorized(@NonNull String authToken) {
        log.info("isAuthorized({})", authToken);
        return !tokenParser.parseClaimsJws(authToken).getSignature().isEmpty();
    }

    /**
     * Gets the registered authentication for the given json web token.
     *
     * @param token scoped json web token.
     * @return registered authentication.
     */
    public Authentication getAuthentication(@NonNull String token) {
        log.info("getAuthentication({})", token);
        Claims claims = tokenParser.parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        org.springframework.security.core.userdetails.User principal = new
                org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public Claims resolveClaims(String token, String jwtSecret) {
        log.info("resolveClaims({},{})", token, jwtSecret);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
