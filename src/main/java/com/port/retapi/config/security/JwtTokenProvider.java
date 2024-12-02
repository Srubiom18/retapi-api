package com.port.retapi.config.security;

import com.port.retapi.config.security.enums.UserRol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String key;

    @Value("${jwt.validityInMilliseconds}")
    private long validityInMilliseconds;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = key.getBytes();
        this.secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    /**
     * Método para generar un token nuevo válido
     */
    public String createToken(String nickname, UserRol rol) {
        // Creamos un Claims que sera el objeto que contenga la información del usuario
        // En el .setSubject suele ir la información identificatoria del usuario como el id o un carácter único
        Claims claims = Jwts.claims().setSubject(nickname);
        // Y con el .put añadimos información del usuario, como puede ser su rol
        claims.put("role", rol);

        // Vamos a indicar también la fecha actual y la de validez del token
        Date actualDate = new Date();
        // Y ahora creamos la de validez del token sumando los milisegundos (30 min)
        Date expirationDate = new Date(actualDate.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(actualDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Método utilizado para extraer el nickname del token
     */
    public String getNicknameOfToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Método utilizado para obtener el rol del token
     */
    public UserRol getRoleOfToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return UserRol.valueOf(claims.get("role").toString());
    }

    /**
     * Método utilizado para validar si el token es correcto
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
