package com.spring.to_do_app.security;

import com.spring.to_do_app.constants.constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {
    public String extractUserEmail(String token){
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String email){
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[]keyBytes = Decoders.BASE64.decode(constants.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
