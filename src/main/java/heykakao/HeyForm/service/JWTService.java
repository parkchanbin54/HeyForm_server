package heykakao.HeyForm.service;

import io.jsonwebtoken.*;

import java.util.Date;

public class JWTService {
    // secret_key
    public static String SECRET_KEY = "heyform";
    private static long tokenValidMilisecond = 10000L * 60 * 60;

    public String createToken(String key, String tmp) {
        var claims = Jwts.claims().setId(key);
        claims.put("email",tmp);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();  }

    public Jws<Claims> getClaims(String jwt, String key) {
        try {
            return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt);    }
        catch(SignatureException e) {      return null;    }  }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getKey(Jws<Claims> claims) {
        return claims.getBody()
                .getId();
    }
    public Object getClaims(Jws<Claims> claims, String key) {
        return claims.getBody()
                .get(key);
    }

}
