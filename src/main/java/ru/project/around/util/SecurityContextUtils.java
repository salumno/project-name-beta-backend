package ru.project.around.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.project.around.security.authentication.JwtAuthentication;
import ru.project.around.util.jwt.JwtTokenUtils;

@Component
public class SecurityContextUtils {
    private final JwtTokenUtils jwtTokenUtils;

    @Autowired
    public SecurityContextUtils(final JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    public Long getUserIdFromCurrentAuthToken() {
        final JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return jwtTokenUtils.getUserId(jwtAuthentication.getName());
    }
}
