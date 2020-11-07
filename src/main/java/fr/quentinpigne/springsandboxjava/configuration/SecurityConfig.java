package fr.quentinpigne.springsandboxjava.configuration;

import fr.quentinpigne.springsandboxjava.utils.security.JwtPreAuthenticatedProcessingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtPreAuthenticatedProcessingFilter jwtPreAuthenticatedProcessingFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery)
        http.csrf().disable();

        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()//
            .mvcMatchers(HttpMethod.GET, "/test/**").hasRole("ADMIN")
            // Disallow everything else...
            .anyRequest().authenticated();

        // Apply JWT
        http.addFilter(jwtPreAuthenticatedProcessingFilter);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring()//
            .mvcMatchers("/v2/api-docs")//
            .mvcMatchers("/swagger-resources/**")//
            .mvcMatchers("/swagger-ui/**");
    }
}
