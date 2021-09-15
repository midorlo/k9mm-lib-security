package com.midorlo.k9.web.rest.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.midorlo.k9.configuration.security.SecurityConstants;
import com.midorlo.k9.service.security.internal.JwtProvider;
import com.midorlo.k9.web.rest.security.model.LoginDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.midorlo.k9.configuration.security.SecurityConstants.AUTHORIZATION_HEADER_FIELD_NAME;

/**
 * Dedicated Controller to handle login requests.
 */
@RestController
@RequestMapping("/security")
public class SecurityController {

    private final JwtProvider                  jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * @param jwtProvider                  issuing a token on successful authentification.
     * @param authenticationManagerBuilder checks the given login credentials against the database.
     */
    public SecurityController(JwtProvider jwtProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.jwtProvider                  = jwtProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken unverifiedToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        );
        Authentication verifiedAuthentication = authenticationManagerBuilder.getObject()
                                                                            .authenticate(unverifiedToken);
        SecurityContextHolder.getContext()
                             .setAuthentication(verifiedAuthentication);

        String newJwtIssue = jwtProvider.createNewToken(verifiedAuthentication,
                                                        loginDto.isRememberMe());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(SecurityConstants.AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_FIELD_NAME + newJwtIssue);
        return new ResponseEntity<>(new JWTToken(newJwtIssue), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
