package services;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import java.util.Map;

public class CodesService {

    private static final JWTSigner signer = new JWTSigner("TACS2019");

    private static final JWTVerifier jwtverifier = new JWTVerifier("TACS2019");

    public static Object parse(String token, String value) throws Exception {
        Map<String, Object> map = CodesService.decodeMapFromToken(token);
        return Long.parseLong((String) String.valueOf(map.get(value)));
    }

    public static String generateTokenFromMap(Map<String,Object> map) {
        return signer.sign(map);
    }

    public static Map<String, Object> decodeMapFromToken(String token) throws Exception {
        return jwtverifier.verify(token);
    }

}
