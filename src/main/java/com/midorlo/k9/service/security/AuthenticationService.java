package com.midorlo.k9.service.security;

import com.midorlo.k9.configuration.security.SecurityProperties;
import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.AuditorAwareUserDetailsImpl;
import com.midorlo.k9.model.security.LoginDto;
import com.midorlo.k9.model.security.mapper.AuthorityMapper;
import com.midorlo.k9.repository.security.AccountRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;

@Service
@Slf4j
public class AuthenticationService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final SecurityProperties securityProperties;
    private final TokenService tokenService;
    private final Key encryptionKey;
    private final JwtParser tokenParser;

    public AuthenticationService(
            AuthenticationManagerBuilder authenticationManagerBuilder,
            AccountRepository accountRepository,
            SecurityProperties securityProperties,
            TokenService tokenService
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.accountRepository = accountRepository;
        this.securityProperties = securityProperties;
        this.tokenService = tokenService;
        this.encryptionKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.securityProperties.getKey()));
        this.tokenParser = Jwts.parserBuilder().setSigningKey(encryptionKey).build();
    }

    /**
     * Handles a login Request.
     *
     * @param loginDto login request body.
     * @return login response.
     */
    public ResponseEntity<Account> handleLogin(LoginDto loginDto) {
        Authentication authentication = authenticate(loginDto);
        Account account = ((AuditorAwareUserDetailsImpl) authentication.getPrincipal()).getAccount();
        String token = tokenService.createNewAuthenticationToken(account, loginDto.isRemember());

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(account);
    }

    /**
     * Tries(!) to authenticate a login Request Body.
     *
     * @param dto dto containing the login request.
     * @return Authentication.
     */
    public Authentication authenticate(LoginDto dto) {
        return authenticationManagerBuilder
                .getObject()
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getEmail(),
                                dto.getPassword()
                        )
                );
    }

    /**
     * Authenticates the given request by looking for - and parsing the request's jwt.
     *
     * @param request the request.
     */
    public void authenticate(HttpServletRequest request) {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String email = null;

        if (StringUtils.hasText(header)
                && header.startsWith("Bearer ")
                && header.contains(" ")
                && header.split(" ").length > 1) {
            final String token = header.split(" ")[1].trim();
            Claims claims = tokenService.resolveClaims(token, securityProperties.getKey());
            if (claims != null) {
                String subject = claims.getSubject();
                if (subject != null) {
                    String[] split = subject.split(",");
                    if (split.length > 1) {
                        email = split[1];
                    }
                }
            }

            //toto smells
            accountRepository.findAccountByEmail(email).ifPresent(account -> {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(account, null, AuthorityMapper.getAuthorities(account));
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        }
    }
}
