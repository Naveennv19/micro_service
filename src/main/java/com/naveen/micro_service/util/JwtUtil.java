package com.naveen.micro_service.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your-secret-key-which-should-be-at-least-256-bits-long";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        try {
            System.out.println("Token received: " + token);
    
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build();
            System.out.println("Parser built successfully");
    
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            System.out.println("Token parsed successfully");
    
            String email = claimsJws.getBody().getSubject();
            System.out.println("Extracted email (subject): " + email);
    
            return email;
        } catch (JwtException e) {
            System.out.println("JWT parsing failed: " + e.getMessage());
            throw new RuntimeException("Invalid or expired JWT token", e);
        }
    }
    

    public boolean validateToken(String token, String email) {
        try {
            return extractEmail(token).equals(email) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }


}
