package Model;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
public class Authentication {

    private static final int TOKEN_VALIDITY = 30; //in minutes
    private static final String SECRET_KEY = "mysecretkey";
    private DataBase database;

    public Authentication(DataBase database) {
        this.database = database;
    }

    public String authenticate(String username, char[] password) {
        if (database.checkCredentials(username, password)) {
            Date expiration = Date.from(Instant.now().plusSeconds(TOKEN_VALIDITY * 60));
            return Jwts.builder()
                    .setSubject(username)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                    .compact();
        } else {
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
