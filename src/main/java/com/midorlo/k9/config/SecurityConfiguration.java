package com.midorlo.k9.config;

import com.midorlo.k9.service.security.AccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Configure Spring Security.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AccountsService accountsService;
//    private final AuthenticationFilter authenticationFilter;
//    private final AuthorizationFilter authorizationFilter;

    public SecurityConfiguration(AccountsService accountsService
//                                 AuthenticationFilter authenticationFilter,
//                                 AuthorizationFilter authorizationFilter
    ) {

        this.accountsService = accountsService;
//        this.authenticationFilter = authenticationFilter;
//        this.authorizationFilter = authorizationFilter;
    }

    /**
     * Configure password encoding schema
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("passwordEncoder()");
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure Cors.
     *
     * @implNote Will only be invoked if cors is enabled.
     */
    @Bean
    public CorsFilter corsFilter() {
        log.info("corsFilter()");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Provide a way to authenticate against a (Spring Security) UserDetails record.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("configure({})", auth);
        auth.userDetailsService(login -> accountsService
                .findAccountByEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException(login)));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        log.info("configure({})", web);
        super.configure(web);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("configure({})", http);
        http
                // Enable CORS
                .cors()
                .and()

                // Disable CSRF
                .csrf()
                .disable()

                // Set session management to stateless
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Set unauthorized requests exception handler
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()))
                .and()

                // Set authorization rules
                .authorizeRequests()
                .antMatchers("/**").anonymous()
                .and()
//                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(authorizationFilter, AuthenticationFilter.class)
        ;
    }

    /**
     * Exposes the authentication manager bean.
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
