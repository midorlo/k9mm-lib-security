package com.midorlo.k9.configuration.security;

import com.midorlo.k9.component.security.JwtAccessDecisionManager;
import com.midorlo.k9.component.security.JwtAuthenticationFilter;
import com.midorlo.k9.component.security.JwtAuthorizationManager;
import com.midorlo.k9.service.security.internal.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl  userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthorizationManager  authorizationManager;
    private final JwtAccessDecisionManager accessDecisionManager;

    public WebSecurityConfigurerAdapterImpl(UserDetailsServiceImpl userDetailsService,
                                            JwtAuthenticationFilter jwtAuthenticationFilter,
                                            JwtAuthorizationManager authorizationManager,
                                            JwtAccessDecisionManager accessDecisionManager) {
        this.userDetailsService      = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authorizationManager    = authorizationManager;
        this.accessDecisionManager = accessDecisionManager;
    }

    /**
     * Configure Cors. Will only be invoked if cors is enabled.
     */
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

    /**
     * Provide a way to authenticate against a (Spring Security) UserDetails record.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        log.info("configure({})", auth);
        auth.userDetailsService(userDetailsService);
    }

    /**
     * Configure {@link WebSecurity}.
     * <p>
     * Endpoints specified in this method will be ignored by Spring Security, meaning it
     * will not protect them from CSRF, XSS, Clickjacking, and so on.
     * <p>
     * By implementation, we will handle as such:
     * <ul>
     * <li>All OPTION requests, as they are used for discovery.</li>
     * <li>The swagger-ui endpoint, as it is served for documentation purposes.</li>
     * <li>The test/* endpoints, as they provide health status information.</li>
     * </ul>
     */
    @Override
    public void configure(WebSecurity web) {
        log.info("configure({})", web);
        web.ignoring()
           .antMatchers(HttpMethod.OPTIONS, "/**")
           .antMatchers(HttpMethod.GET, "/**")
           .antMatchers(HttpMethod.POST, "/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.info("configure({})", http);
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

                // Set unauthorized requests exception handler

                .authorizeRequests()
                .antMatchers("/**")
                .fullyAuthenticated()
                .and()
                .httpBasic();
                // Inject JWT Authentication
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
