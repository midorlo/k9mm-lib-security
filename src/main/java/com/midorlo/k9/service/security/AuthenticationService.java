package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Account;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.midorlo.k9.service.security.AuthenticationService.K9FilterUtilities.resolveClaims;

/**
 * Authenticates a {@link HttpServletRequest} and continues the filter chain.
 */
@SuppressWarnings("unused")
@Slf4j
@Service
public class AuthenticationService {

    private final AccountsService accountsService;

    @Value("${k9.security.secret}")
    private String jwtSecret;
    @Value("${k9.fqdn}")
    private String jwtIssuer;


    public AuthenticationService(AccountsService accountsService) {
        this.accountsService = accountsService;
    }


    private String resolveAuthorization(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String u = null;
        if (StringUtils.hasText(header)
                && header.startsWith("Bearer ")
                && header.contains(" ")
                && header.split(" ").length > 1) {
            final String token = header.split(" ")[1].trim();
            Claims claims = resolveClaims(token, jwtSecret);
            if (claims != null) {
                String subject = claims.getSubject();
                if (subject != null) {
                    String[] split = subject.split(",");
                    if (split.length > 1) {
                        u = split[1];
                    }
                }
            }
        }
        return u;
    }

    public void authenticate(HttpServletRequest request) {
        String email = resolveAuthorization(request);
        if (email != null) {
            accountsService.findAccountByEmail(email).ifPresent(account -> setSecurityContext(request, account));

        }
    }

    private void setSecurityContext(HttpServletRequest request, Account account) {
        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @SuppressWarnings("unused")
    static final class K9FilterUtilities {

        static Claims resolveClaims(String token, String jwtSecret) {
            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        static String generateAccessToken(Account account, String jwtIssuer, String jwtSecret) {
            return Jwts.builder()
                    .setSubject(String.format("%s,%s", account.getId(), account.getEmail()))
                    .setIssuer(jwtIssuer)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                    .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                    .compact();
        }


        static String resolveAccountId(String token, String jwtSecret) {
            return resolveClaims(token, jwtSecret).getSubject()
                    .split(",")[0];
        }


        static Date resolveExpirationDate(String token, String jwtSecret) {
            return resolveClaims(token, jwtSecret).getExpiration();
        }

        static boolean validate(String token, String jwtSecret) {
            try {
                return resolveClaims(token, jwtSecret) != null;
            } catch (SecurityException ex) {
                log.error("Invalid JWT signature - {}", ex.getMessage());
            } catch (MalformedJwtException ex) {
                log.error("Invalid JWT token - {}", ex.getMessage());
            } catch (ExpiredJwtException ex) {
                log.error("Expired JWT token - {}", ex.getMessage());
            } catch (UnsupportedJwtException ex) {
                log.error("Unsupported JWT token - {}", ex.getMessage());
            } catch (IllegalArgumentException ex) {
                log.error("JWT claims string is empty - {}", ex.getMessage());
            }
            return false;
        }
    }

    public boolean validate(String token, String jwtSecret) {
        try {
            return resolveClaims(token, jwtSecret) != null;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }


}
