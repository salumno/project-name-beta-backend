package ru.project.around.util.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import ru.project.around.model.TokenTypeEnum;
import ru.project.around.model.User;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenUtils {

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.jwt.exp.refresh}")
    private long refreshTokenExpirationTime;

    @Value("${auth.jwt.exp.access}")
    private long accessTokenExpirationTime;

    public boolean isTokenValid(final String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String createAccessToken(final User user) {
        return createToken(user, TokenTypeEnum.ACCESS);
    }

    public String createRefreshToken(final User user) {
        return createToken(user, TokenTypeEnum.REFRESH);
    }

    public Long getUserId(final String token) {
        return Long.valueOf(getTokenClaims(token).getId());
    }

    public String getUserPhone(final String token) {
        return getTokenClaims(token).getSubject();
    }

    private String createToken(final User user, final TokenTypeEnum token) {
        final long additionalTime = TokenTypeEnum.REFRESH.equals(token) ? refreshTokenExpirationTime : accessTokenExpirationTime;
        final Date expDate = Date.from(Instant.now().plusMillis(additionalTime));
        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getPhone())
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private boolean isTokenExpired(final String token) {
        final Date tokenExpirationDate = getTokenClaims(token).getExpiration();
        return getCurrentDate().after(tokenExpirationDate);
    }

    private Claims getTokenClaims(final String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (MalformedJwtException | SignatureException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("Invalid token");
        }
    }

    private Date getCurrentDate() {
        return Date.from(Instant.now());
    }
}
