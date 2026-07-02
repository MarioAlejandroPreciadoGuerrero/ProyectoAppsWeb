package com.example.Bemole_API.service.security;

import com.example.Bemole_API.models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final String jwtSecret;
    private final Long jwtExpiration;

    public JwtService(@Value("${security.jwt.secret}") String jwtSecret, @Value("${security.jwt.expiration}") long jwtExpiration) {
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(
                ahora.getTime() + jwtExpiration
        );

        Map<String, Object> claims = Map.of(
                "usuarioId", usuario.getId(),
                "rol", usuario.getRol().name()
        );

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getEmail())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(obtenerClave())
                .compact();
    }

    public String obtenerEmail(String token) {
        return obtenerClaims(token).getSubject();
    }

    public Long obtenerUsuarioId(String token) {
        Number usuarioId = obtenerClaims(token)
                .get("usuarioId", Number.class);

        return usuarioId.longValue();
    }

    public String obtenerRol(String token) {
        return obtenerClaims(token)
                .get("rol", String.class);
    }

    public boolean esTokenValido(
            String token,
            Usuario usuario
    ) {
        String email = obtenerEmail(token);

        return email.equalsIgnoreCase(usuario.getEmail())
                && !estaExpirado(token);
    }

    public long obtenerExpiracionEnSegundos() {
        return jwtExpiration / 1000;
    }

    private boolean estaExpirado(String token) {
        Date expiracion = obtenerClaims(token)
                .getExpiration();

        return expiracion.before(new Date());
    }

    private Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClave())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey obtenerClave() {
        byte[] bytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(bytes);
    }
}
