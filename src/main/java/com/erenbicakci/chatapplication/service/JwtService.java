package com.erenbicakci.chatapplication.service;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Log4j2
@Component
public class JwtService {


    @Value("${secret-key}")
    private String SECRET_KEY;



    public String findUsername(String token) {
        try {
           return exportToken(token, Claims::getSubject);
        }
        catch (Exception e){
            return null;
        }

    }





    private <T> T exportToken(String token, Function<Claims,T> claimsTFunction) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();

        return claimsTFunction.apply(claims);
    }


    private Key getKey() {
        byte[] key= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean tokenControl(String jwt, UserDetails userDetails) {
        final String username = findUsername(jwt);
        return (username.equals(userDetails.getUsername()) && !exportToken(jwt,Claims::getExpiration).before(new Date()));
    }

    public String findUsernameFromTokenAndControl(String jwt) {
        final String username = findUsername(jwt);
        try {
            if (!exportToken(jwt,Claims::getExpiration).before(new Date())) {
                return username;
            }
        }
        catch (Exception e){
            return null;
        }

        return null;
    }

    public String generateToken(UserDetails user) {

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+100000*60*24))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}

