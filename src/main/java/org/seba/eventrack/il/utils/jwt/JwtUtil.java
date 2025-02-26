package org.seba.eventrack.il.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.seba.eventrack.dl.entities.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    private final byte[] secret = "Yabadabadoooooooooooooooooooooooooooooo".getBytes();

    private SecretKey getSecretKey() {
        return new SecretKeySpec(secret, "HmacSHA256");
    }

    private final int expireAt = 86400; // 1Day in seconds

    private final JwtBuilder builder;

    private final JwtParser parser;

    public JwtUtil() {
        this.builder = Jwts.builder().signWith(this.getSecretKey());
        this.parser = Jwts.parserBuilder().setSigningKey(this.getSecretKey()).build();
    }

    public String generateToken(User user) {
        return this.builder
                .setSubject(user.getUsername())
                .claim("id",user.getId())
                .claim("role",user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireAt * 1000L))
                .compact();
    }

    private Claims getClaims(String token) {
        return this.parser.parseClaimsJws(token).getBody();
    }

    public String getUsername(String token) {
        return this.getClaims(token).getSubject();
    }

    public Long getUserId(String token) {
        return this.getClaims(token).get("id", Long.class);
    }

    public String getRole(String token) {
        return this.getClaims(token).get("role", String.class);
    }

    public boolean isValid(String token) {
        Claims claims = getClaims(token);
        Date now = new Date();
        return now.after(claims.getIssuedAt()) && now.before(claims.getExpiration());
    }
}
