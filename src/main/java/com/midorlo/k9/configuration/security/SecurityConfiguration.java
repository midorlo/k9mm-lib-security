package com.midorlo.k9.configuration.security;

//import com.midorlo.k9.component.security.JwtAccessDecisionManager;

import com.midorlo.k9.web.filter.JwtAuthenticationFilter;
//import com.midorlo.k9.component.security.JwtAuthorizationManager;
import com.midorlo.k9.service.security.internal.UserDetailsServiceImpl;
import com.midorlo.k9.web.filter.ClearanceAuthorizationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static com.midorlo.k9.configuration.security.AuditorAwareImpl.AUDITOR_AWARE_IMPL_INSTANCE_NAME;

/**
 * Configure Spring Security.
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = AUDITOR_AWARE_IMPL_INSTANCE_NAME)
@EnableWebSecurity

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl           userDetailsService;
    private final JwtAuthenticationFilter      jwtAuthenticationFilter;
    private final ClearanceAuthorizationFilter clearanceAuthorizationFilter;

    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService,
                                 JwtAuthenticationFilter jwtAuthenticationFilter,
                                 ClearanceAuthorizationFilter clearanceAuthorizationFilter) {


        this.userDetailsService           = userDetailsService;
        this.jwtAuthenticationFilter      = jwtAuthenticationFilter;
        this.clearanceAuthorizationFilter = clearanceAuthorizationFilter;
    }

    @Bean
    public CorsFilter corsFilter() {
        log.info("corsFilter()");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration               config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("Configuring http security from {}", getClass().getName());
        http
                // Disable CORS
                .cors()
                .disable()

                // Disable CSRF
                .csrf()
                .disable()

                // Stateless Sessions
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Deny everything by default
                .authorizeRequests()
                    .antMatchers("/**")
                    .permitAll()
                    .and()

                // Inject JWT Authentication
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Inject path and method based authorization
                .addFilterAfter(clearanceAuthorizationFilter, JwtAuthenticationFilter.class)

                .httpBasic();
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
