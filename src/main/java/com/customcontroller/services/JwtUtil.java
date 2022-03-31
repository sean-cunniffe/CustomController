package com.customcontroller.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.TokenType;
import com.customcontroller.entity.User;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.Provider;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Util that uses HS256
 * Used https://github.com/auth0/java-jwt to build
 * <br><br>
 * Created by SeanCunniffe on 12/Jan/2022
 */

@ApplicationScoped
public class JwtUtil {

    private final Algorithm algorithm;
    private final String issuer = getClass().getSimpleName();
    private final JWTVerifier verifier;
    private final int refreshTokenLife;
    private final int accessTokenLife;
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String ACCESS_TOKEN = "accessToken";

    /**
     * Util for creating and validating refresh and access tokens.
     *
     * @param aRefreshTokenLife token life of refresh token.
     * @param aAccessTokenLife  token life of access token
     */
    public JwtUtil(int aRefreshTokenLife, int aAccessTokenLife) {
        accessTokenLife = aAccessTokenLife;
        refreshTokenLife = aRefreshTokenLife;
//        SecureRandom random = new SecureRandom();
//        byte[] algorithmSecret = new byte[32];
//        random.nextBytes(algorithmSecret);
        byte[] algorithmSecret =
                new byte[]{34, 34, 56, 78, 75, 53, 56, 34, 56, 78, 90, 12,
                        34, 56, 78, 90, 90, 87, 65, 43, 21, 45, 67, 4, 3,
                        2, 6, 76, 4, 3, 22, 99};
        algorithm = Algorithm.HMAC256(algorithmSecret);
        verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    /**
     * Util for creating and validating refresh and access tokens.
     * <br>
     * Default refresh token life is 5,184,000 seconds (60 days)
     * Default access token life is 604,800 seconds (7 days)
     */
    public JwtUtil() {
        this(5_184_000, 604_800);
    }

    private String createToken(User user, TokenType type, int tokenLife) {
        Date expireDate = Date.from(Instant.now().plusSeconds(tokenLife));
        Map<String, Object> header = getHeader(type);
        return JWT.create()
                .withHeader(header)
                .withIssuer(issuer)
                .withClaim("ROLE", user.getRole().toString())
                .withClaim("email", user.getEmail())
                .withIssuedAt(Date.from(Instant.now()))
                .withExpiresAt(expireDate)
                .sign(algorithm);
    }

    /**
     * Create refreshToken from user entity
     *
     * @param user
     * @return a token with the expire date, the user info
     */
    public String createRefreshToken(User user) {
        return createToken(user, TokenType.REFRESH, refreshTokenLife);
    }

    /**
     * Create access token from user entity and the refresh token
     *
     * @param user         The entity who used refresh token to create access token
     * @param refreshToken The refresh token used to create access token
     * @return a string which is the access token
     */
    public String createAccessToken(User user, String refreshToken) {
        // valid its the users refreshToken
        validateToken(refreshToken, TokenType.REFRESH);
        // create access token
        return createToken(user, TokenType.ACCESS, accessTokenLife);
    }

    public String getEmailFromToken(String token){
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }


    public ROLE getRoleFromToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        String role = jwt.getClaim("ROLE").asString();
        return ROLE.valueOf(role);
    }

    private Map<String, Object> getHeader(TokenType tokenType) {
        Map<String, Object> header = new HashMap<>();
        header.put("type", tokenType);
        header.put("alg", "HS256");
        return header;
    }

    public boolean validateToken(String token, TokenType tokenType) {
        DecodedJWT jwt;
        jwt = verifier.verify(token);
        return isTokenType(jwt, tokenType);
    }

    private boolean isTokenType(DecodedJWT jwt, TokenType type) {
        String tS = jwt.getHeaderClaim("type").asString();
        TokenType typ = TokenType.valueOf(tS);
        boolean isTokenType = typ.equals(type);
        if (!isTokenType)
            throw new JWTVerificationException("Invalid token type.");
        else
            return true;
    }
}
