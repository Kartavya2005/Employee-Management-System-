package com.Project.Ems.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    // Generate Secret Key
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );
    }
    // Generate JWT Token
    public String generateToken(
            String email,
            String role
    ) {
        log.debug("Generating JWT token for email: {}", email);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // Extract Email
    public String extractEmail(
            String token
    ) {
        log.debug("Extracting email from JWT token");
        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    // Generic Claim Extractor
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        log.debug("Extracting claim from JWT token");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract All Claims
    public Claims extractAllClaims(
            String token
    ) {
        log.debug("Extracting all claims from JWT token");
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract Expiration
    private Date extractExpiration(
            String token
    ) {
        log.debug("Extracting expiration from JWT token");
        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    // Check Expiration
    private boolean isTokenExpired(
            String token
    ) {
        log.debug("Checking expiration of JWT token");
        return extractExpiration(token)
                .before(new Date());
    }

    // Validate Token
    public boolean validateToken(
            String token,
            String email
    ) {
        log.info("Validating JWT token for email: {}", email);
        final String tokenEmail =
                extractEmail(token);
        log.info("Token email: {}, Email: {}", tokenEmail, email);
        return tokenEmail.equals(email)
                && !isTokenExpired(token);
    }
}