package fr.quentinpigne.springsandboxjava.utils.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Component
public class JwtPreAuthenticatedProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {

    public JwtPreAuthenticatedProcessingFilter(JwtAuthenticationUserDetailsService jwtAuthenticationUserDetailsService) {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(jwtAuthenticationUserDetailsService);
        this.setAuthenticationManager(new ProviderManager(Collections.singletonList(preAuthenticatedAuthenticationProvider)));
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return "N/A";
    }
}
