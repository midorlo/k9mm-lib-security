package com.midorlo.k9.service.security.internal;

import com.midorlo.k9.configuration.security.SecurityProperties;
import com.midorlo.k9.util.RuntimeUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private final Key       key;
    private final JwtParser jwtParser;
    private final long      tokenValidityMs;
    private final long      tokenValidityExtendedMs;


    public JwtProvider(SecurityProperties securityProperties) {
        this.key                     = parsePrivateKey(securityProperties.getKey());
        this.jwtParser               = Jwts.parserBuilder().setSigningKey(key).build();
        this.tokenValidityMs         = 1000 * securityProperties.getValidityMs();
        this.tokenValidityExtendedMs = 1000 * securityProperties.getExtendedValidityMs();
    }

    /**
     * Issues a new Jwt. Should be only access from the Rest controller after successful authentification.
     *
     * @param authentication verified {@link UsernamePasswordAuthenticationToken}
     * @param rememberMe     indicates the request for extended lifetime of the token creating.
     * @return jwt created.
     */
    public String createNewToken(Authentication authentication, boolean rememberMe) {

        String authorities = authentication.getAuthorities()
                                           .stream()
                                           .map(GrantedAuthority::getAuthority)
                                           .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = (rememberMe)
                        ? new Date(now + this.tokenValidityExtendedMs * 1000)
                        : new Date(now + this.tokenValidityMs * 1000);

        return Jwts
                .builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * Parses the given jwt, resolving a fully fledged authentification.
     *
     * @param token request's user's jwt.
     * @return user's authentication.
     * @implNote Since the token is signed, it cannot be modified. Since it cannot be modified, its content can be
     * trust. Since its content can be trust,  we do not need a single database query to create the full authentication.
     */
    public Authentication resolveAuthentication(String token) {

        Claims claims = jwtParser.parseClaimsJws(token).getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY)
                                                                                 .toString()
                                                                                 .split(","))
                                                                   .filter(auth -> !auth.trim().isEmpty())
                                                                   .map(SimpleGrantedAuthority::new)
                                                                   .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean isTokenValid(String authToken) {
        boolean isValid = StringUtils.hasText(authToken);
        if (isValid) {
            try {
                jwtParser.parseClaimsJws(authToken);
            } catch (JwtException | IllegalArgumentException e) {
                log.info("Invalid JWT token.");
                log.trace("Invalid JWT token trace.", e);
                isValid = false;
            }
        }
        return isValid;
    }

    private Key parsePrivateKey(String secret) {
        byte[] keyBytes;
        if (!ObjectUtils.isEmpty(secret)) {
            keyBytes = Decoders.BASE64.decode(secret);
        } else {
            if (RuntimeUtils.isProfileActive("PRD")) {
                throw new SecurityException("Insufficient jwt key!");
            } else {
                log.warn("Using non encoded jwt key!");
            }
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
