package com.kjs990114.goodong.common.jwt;

import com.kjs990114.goodong.common.exception.ErrorException;
import com.kjs990114.goodong.common.exception.Error;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expiredMs = 60 * 60 * 60 * 60 * 60 * 10L;
    public JwtUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public void validateToken(String bearer){
        try {
            String token = bearer.split(" ")[1];
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        }catch (SecurityException | MalformedJwtException e){
            throw new ErrorException(Error.INVALID_TOKEN);
        }catch (ExpiredJwtException e){
            throw new ErrorException(Error.EXPIRED_TOKEN);
        }catch (Exception e){
            throw new ErrorException(Error.WRONG_TOKEN);
        }

    }

    public Long getUserId(String bearer) {
        String token = bearer.split(" ")[1];
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }



    public String createJwt(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
