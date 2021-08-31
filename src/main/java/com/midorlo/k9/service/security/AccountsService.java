package com.midorlo.k9.service.security;

import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.repository.security.AccountRepository;
import com.midorlo.k9.service.security.dto.LoginDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;


@Service
@Slf4j
public class AccountsService {

    private final AccountRepository accountRepository;

    @Value("${k9.security.secret}")
    private String jwtSecret;

    public AccountsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createIfNotExists(Account account) {
        return accountRepository.findAccountByEmail(account.getEmail()).orElse(accountRepository.save(account));
    }

    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findAccountByEmail(email);
    }

    /**
     * Get the login of the current account.
     *
     * @return the login of the current account.
     */
    public Optional<String> getPrincipal() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(getPrincipal(securityContext.getAuthentication()));
    }

    /**
     * Get the login of the current account by its authentication.
     *
     * @return the login of the current account.
     */
    private String getPrincipal(Authentication authentication) {
        String principal = null;
        if (authentication != null && authentication.getPrincipal() != null) {
            principal = (authentication.getPrincipal() instanceof String)
                    ? (String) authentication.getPrincipal()
                    : ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return principal;
    }

    String resolveAuthorization(HttpServletRequest request) {
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
            findAccountByEmail(email).ifPresent(account -> setSecurityContext(request, account));

        }
    }

    private void setSecurityContext(HttpServletRequest request, Account account) {
        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public Authentication authenticate(LoginDto request) {
        throw new RuntimeException("todo impl" + request);
    }

    public String generateAccessToken(Account account) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", account.getId(), account.getEmail()))
                .setIssuer("todo change")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .compact();
    }

    static Claims resolveClaims(String token, String jwtSecret) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

//    static String resolveAccountId(String token, String jwtSecret) {
//        return resolveClaims(token, jwtSecret).getSubject()
//                .split(",")[0];
//    }
//
//
//    static Date resolveExpirationDate(String token, String jwtSecret) {
//        return resolveClaims(token, jwtSecret).getExpiration();
//    }
//
//    static boolean validate(String token, String jwtSecret) {
//        try {
//            return resolveClaims(token, jwtSecret) != null;
//        } catch (SecurityException ex) {
//            log.error("Invalid JWT signature - {}", ex.getMessage());
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token - {}", ex.getMessage());
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token - {}", ex.getMessage());
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token - {}", ex.getMessage());
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty - {}", ex.getMessage());
//        }
//        return false;
//    }
}
