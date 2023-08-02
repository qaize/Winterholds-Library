package com.example.AppWinterhold.Utility;

import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class JwtToken {

    @Getter
    private String audience = "AppQaize";
    @Getter
    private String secret_key = "ikhwani-Syahbana-Qaize13";

    private Claims getClaims(String token){
        JwtParser parser = Jwts.parser().setSigningKey(secret_key);
        Jws<Claims> signatureClaims = parser.parseClaimsJws(token);
        Claims claims = signatureClaims.getBody();
        return claims;
    }
    public String getUsername(String token){

        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    public String generateToken(String subject, String secret_key,String audience, String username){
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder = jwtBuilder.setSubject(subject)
                .claim("username",username)
                .setIssuer("http://localhost:7081")
                .setAudience(audience)
                .setExpiration(Date.valueOf(LocalDate.now().plusDays(1)))
                .signWith(SignatureAlgorithm.HS256,secret_key);
        return jwtBuilder.compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        Claims claims = getClaims(token);
        String user = claims.get("username",String.class);
        String audienceUser = claims.getAudience();
        return (user.equals(userDetails.getUsername()) && audienceUser.equals(audience));
    }


}
