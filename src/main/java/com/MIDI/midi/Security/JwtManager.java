package com.MIDI.midi.Security;

import com.MIDI.midi.Configs.keystoreConfig;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
@Component
public class JwtManager {
    @Value("${app.security.jwt.expiration}")
    private final long jwtExpiration;
    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final keystoreConfig keystoreConfig;

    public JwtManager(@Value("${app.security.jwt.expiration}")long jwtExpiration, RSAPrivateKey privateKey, RSAPublicKey publicKey, com.MIDI.midi.Configs.keystoreConfig keystoreConfig) {
        this.jwtExpiration = jwtExpiration;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.keystoreConfig = keystoreConfig;
    }

    public String generateToken( UserDetails subject) {
        return Jwts
                .builder().setSubject(subject.getUsername())
                .setIssuer("Midi Corporation")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(keystoreConfig.getJwtSigningPrivateKey(), SignatureAlgorithm.RS256).compact();
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(keystoreConfig.getJwtSigningPrivateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}