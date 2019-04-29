package ru.project.around.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.project.around.security.authentication.JwtAuthentication;
import ru.project.around.security.details.UserDetailsImpl;
import ru.project.around.util.jwt.JwtTokenUtils;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtils jwtTokenUtils;

    public JwtAuthenticationProvider(final JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        final String token = jwtAuthentication.getName();
        if (!jwtTokenUtils.isTokenValid(token)) {
            throw new AuthenticationServiceException("Invalid token");
        }
        final UserDetails userDetails = new UserDetailsImpl(
                jwtTokenUtils.getUserId(token),
                jwtTokenUtils.getUserPhone(token)
        );

        jwtAuthentication.setUserDetails(userDetails);
        jwtAuthentication.setAuthenticated(true);
        return jwtAuthentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthentication.class.getName().equals(aClass.getName());
    }
}