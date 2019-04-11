package ru.project.around.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.project.around.security.authentication.JwtAuthentication;
import ru.project.around.security.details.UserDetailsImpl;

@Component
public class SecurityContextUtils {
    public Long getUserIdFromCurrentAuthToken() {
        final JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        final UserDetailsImpl userDetails = (UserDetailsImpl) jwtAuthentication.getDetails();
        return userDetails.getUserId();
    }
}
