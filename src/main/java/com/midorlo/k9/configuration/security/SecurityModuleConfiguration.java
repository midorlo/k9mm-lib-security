package com.midorlo.k9.configuration.security;

import com.midorlo.k9.components.security.AuthenticationFilter;
import com.midorlo.k9.domain.security.Account;
import com.midorlo.k9.model.security.AuditorAwareUserDetailsImpl;
import com.midorlo.k9.service.security.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;

import static com.midorlo.k9.components.security.AuditorAwareImpl.AUDITOR_AWARE_IMPL_INSTANCE_NAME;

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
public class SecurityModuleConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final AccountService accountService;
    private final AuthenticationFilter authenticationFilter;

    public SecurityModuleConfiguration(AccountService accountService, AuthenticationFilter authenticationFilter) {

        this.accountService = accountService;
        this.authenticationFilter = authenticationFilter;
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
        auth.userDetailsService(login -> {
            Account account = accountService.findAccountByEmail(login)
                    .orElseThrow(() -> new UsernameNotFoundException(login));
            return new AuditorAwareUserDetailsImpl(account);
        });
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
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        log.info("addCorsMappings({})", registry);
        registry.addMapping("/**");
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

                // Inject JWT Authentication
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // Inject Custom Authorization
                .authorizeRequests()

                // Leave a single antMatcher as a Spring Security requirement
                .antMatchers("/").anonymous();
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
