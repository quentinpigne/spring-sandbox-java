package fr.quentinpigne.springsandboxjava.utils.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String NAME_CLAIM = "name";
    private static final String AUTHORITIES_CLAIM = "authorities";

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Encoders.BASE64.encode(secretKey.getBytes());
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken preAuthenticatedAuthenticationToken
    ) throws UsernameNotFoundException {
        Claims claims = decodeJwt(resolveToken(preAuthenticatedAuthenticationToken));
        return User.withUsername(claims.get(NAME_CLAIM, String.class)).password("").authorities(getAuthorities(claims)).accountExpired(
            false).accountLocked(false).credentialsExpired(false).disabled(false).build();
    }

    private String resolveToken(AbstractAuthenticationToken authenticationToken) {
        String bearerToken = (String) authenticationToken.getPrincipal();
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Claims decodeJwt(String jwt) {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
        return jwtParser.parseClaimsJws(jwt).getBody();
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        List<String> authorities = claims.get(AUTHORITIES_CLAIM, List.class);
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
