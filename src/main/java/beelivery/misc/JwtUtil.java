package beelivery.misc;

import beelivery.user.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import spark.Request;

import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Optional;

public class JwtUtil {
    private static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static Optional<String> parseJws(Request request)  {
        String auth = request.headers("Authorization");
        if (auth == null || auth.isEmpty() || !auth.contains("Bearer")) {
            return Optional.empty();
        }

        String jws = auth.substring(auth.indexOf("Bearer") + 7);
        if(jws.equalsIgnoreCase("undefined") || auth.length() <= 7) {
            return Optional.empty();
        }
        return Optional.of(jws);
    }

    public static String generateJws(User user) {
        return Jwts.builder()
                .setSubject(user.getId())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .signWith(KEY)
                .compact();
    }

    public static String getUsername(String jws) {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(jws).getBody().getSubject();
    }
}
