package com.ankit.expense_tracker.service;

import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserRepo userRepo;

//    public JwtService() {
//        this.secretKey = generateSecretKey();
//    }

    private String generateSecretKey() {

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

            SecretKey secretKey = keyGen.generateKey();

            return Base64.getEncoder()
                    .encodeToString(secretKey.getEncoded());

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(
                    "Error generating secret key", e);
        }
    }

    public String getAuthenticationToken(String userEmail) {
        User user = userRepo.findUserByEmail(userEmail);
        if(user == null){
            throw new RuntimeException("User not found with email: " + userEmail);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",user.getUserId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(
                        new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + 1000 * 60 * 20))
                .signWith(getKey())
                .compact();
    }

    private Key getKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    public Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimResolver) {

        final Claims claims = extractAllClaims(token);

        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {

        return extractAllClaims(token)
                .get("userId", Long.class);
    }

    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        final String userName =
                extractUserName(token);

        return userName.equals(
                userDetails.getUsername())
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }
}