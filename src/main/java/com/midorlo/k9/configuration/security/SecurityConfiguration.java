package com.midorlo.k9.configuration.security;

import com.midorlo.k9.configuration.security.filters.AuthenticationFilter;
import com.midorlo.k9.configuration.security.filters.AuthorizationFilter;
import com.midorlo.k9.configuration.security.filters.ConfigurationFilter;
import com.midorlo.k9.service.security.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    private final AccountService       accountService;
    private final AuthenticationFilter authenticationFilter;
    private final ConfigurationFilter  configurationFilter;

    @Value("${k9.security.strategy}")
    private String strategyName;

    public SecurityConfiguration(AccountService accountService,
                                 AuthenticationFilter authenticationFilter,
                                 ConfigurationFilter configurationFilter) {

        this.accountService       = accountService;
        this.authenticationFilter = authenticationFilter;
        this.configurationFilter  = configurationFilter;

        SecurityContextHolder.setStrategyName(strategyName);
    }

    /**
     * Configure password encoding schema
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Used by spring security if CORS is enabled.
     */
    @Bean
    public CorsFilter corsFilter() {
        log.info("Configuring CORS filter.");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration               config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * Implement finding the spring security user to authentificate against.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("Configuring AuthenticationManagerBuilder.");
        auth.userDetailsService(username -> accountService.findByName(username)
                                                          .orElseThrow(() -> new UsernameNotFoundException(username)));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        log.info("(Default) Configuring AuthenticationManagerBuilder.");
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("(Default) Configuring HttpSecurity.");
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

                // Set Cost-efficient base filters on endpoints
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/").permitAll()
                .antMatchers("/api/v1").permitAll()
                .antMatchers("/api/v1/apidocs/**").permitAll()
                .antMatchers("/api/v1/public/**").permitAll()
                .antMatchers("/api/v1/secured/**").authenticated()
                .antMatchers("/api/v1/ops/**").fullyAuthenticated()
                .and()
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new AuthorizationFilter(), AuthenticationFilter.class)
                .addFilterAfter(configurationFilter, AuthorizationFilter.class);
    }

    /**
     * Expose authentication manager bean.
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
