package com.john.springbootmall.util;

import com.john.springbootmall.entity.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {

    /**
     * JWT VALIID TIME 1Min
     */
    private static final long EXPIRATION_TIME = 1 * 60 * 1000;
    /**
     * JWT SECRET KEY
     */
    private static final String SECRET = "MySecret";

    /**
     * 簽發JWT
     *
     * @param user
     * @return
     */
    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail()) // 以email當subject
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith( SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 驗證JWT
     */
    public void validateToken(String token) throws AuthException {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token);
        } catch (SignatureException e) {
            throw new AuthException("Invalid JWT signature.");
        }
        catch (MalformedJwtException e) {
            throw new AuthException("Invalid JWT token.");
        }
        catch (ExpiredJwtException e) {
            throw new AuthException("Expired JWT token");
        }
        catch (UnsupportedJwtException e) {
            throw new AuthException("Unsupported JWT token");
        }
        catch (IllegalArgumentException e) {
            throw new AuthException("JWT token compact of handler are invalid");
        }
    }
}
