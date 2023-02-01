package com.john.springbootmall.util;

import com.john.springbootmall.entity.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.security.auth.message.AuthException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {

    /**
     * JWT VALIID TIME 30Min
     */
    private static final long EXPIRATION_TIME = 30 * 60 * 1000;
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
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getUserId());
        userMap.put("role", user.getRole().name());

        return Jwts.builder()
                .setClaims(userMap)
                .setSubject(user.getEmail()) // 以email當subject
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith( SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 解析JWT
     */
    public Map<String, Object> analyzeToken(String token) throws AuthException {
        Map<String, Object> tokenMap = new HashMap<>();

        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();

            tokenMap.put("userId", claims.get("userId"));
            tokenMap.put("role", claims.get("role"));
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
        return tokenMap;
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
