package com.customcontroller.services;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.customcontroller.entity.ROLE;
import com.customcontroller.entity.TokenType;
import com.customcontroller.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    JwtUtil jwtUtil;
    User user;

    @BeforeEach
    void beforeEach(){
        user = mock(User.class);
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getRole()).thenReturn(ROLE.STAFF);
        when(user.getEmail()).thenReturn("johndoe@gmail.com");
    }


    @Test
    void testCreateRefreshTokenAndDecodeItAsValid() {
        jwtUtil = new JwtUtil();
        String token = jwtUtil.createRefreshToken(user);

        boolean valid = jwtUtil.validateToken(token, TokenType.REFRESH);
        assertThat(valid, is(true));
    }

    @Test
    void testRefreshTokenAndDecodeItAsInvalidSignature() {
        jwtUtil = new JwtUtil();
        String token = "eyJ0eXAiOiJSRUZSRVNIIiwiYWxnIjoiSFMyNTYifQ.eyJST0xFIjoiU1RBRkYiLCJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJpc3MiOiJhdXRoMCIsImV4cCI6MTY0MjA3NzAwMCwiaWF0IjoxNjQyMDc2OTk5fQ.V1KiAaH13CzYcu-zIhCTILn6xmMCjktYLZyV2qKSLOE";
        Throwable t = assertThrows(SignatureVerificationException.class, () -> jwtUtil.validateToken(token, TokenType.REFRESH));
        assertThat(t.getMessage(), is("The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256"));
    }

    @Test
    void testRefreshTokenAndDecodeItAsExpired() {
        jwtUtil = new JwtUtil(-1, -1);
        String expiredToken = jwtUtil.createRefreshToken(user);
        Throwable t = assertThrows(TokenExpiredException.class, () -> jwtUtil.validateToken(expiredToken, TokenType.REFRESH));
        assertTrue(t.getMessage().startsWith("The Token has expired"));
    }


    @Test
    void testAccessTokenAndDecodeItAsValid() {
        jwtUtil = new JwtUtil();
        String refreshToken = jwtUtil.createRefreshToken(user);
        String accessToken = jwtUtil.createAccessToken(user, refreshToken);
        boolean valid = jwtUtil.validateToken(accessToken, TokenType.ACCESS);
        assertThat(valid, is(true));
    }

    @Test
    void testCreateAccessTokenWithExpiredRefreshToken() {
        jwtUtil = new JwtUtil(-1, 999);
        String refreshToken = jwtUtil.createRefreshToken(user);
        Throwable t = assertThrows(TokenExpiredException.class, () -> jwtUtil.createAccessToken(user, refreshToken));
        assertTrue(t.getMessage().startsWith("The Token has expired"));
    }

    @Test
    void testCreateAccessTokenWithAnotherAccessToken(){
        jwtUtil = new JwtUtil();
        String refreshToken = jwtUtil.createRefreshToken(user);
        String accessToken = jwtUtil.createAccessToken(user, refreshToken);
        Throwable t = assertThrows(JWTVerificationException.class, ()-> jwtUtil.createAccessToken(user, accessToken));
        assertThat(t.getMessage(), is("Invalid token type."));
    }

    @Test
    void testAccessTokenAndDecodeItAsExpired() {
        jwtUtil = new JwtUtil(999, -1);
        String refreshToken = jwtUtil.createRefreshToken(user);
        String accessToken = jwtUtil.createAccessToken(user, refreshToken);
        Throwable t = assertThrows(TokenExpiredException.class, () -> jwtUtil.validateToken(accessToken, TokenType.ACCESS));
        assertTrue(t.getMessage().startsWith("The Token has expired"));
    }

    @Test
    void testGetEmailFromToken(){
        jwtUtil = new JwtUtil();
        String refreshToken = jwtUtil.createRefreshToken(user);
        String accessToken = jwtUtil.createAccessToken(user, refreshToken);
        String email = jwtUtil.getEmailFromToken(accessToken);
        assertThat(email, is(user.getEmail()));
    }

    @Test
    void testGetRoleFromToken(){
        jwtUtil = new JwtUtil();
        String refreshToken = jwtUtil.createRefreshToken(user);
        String accessToken = jwtUtil.createAccessToken(user, refreshToken);
        ROLE role = jwtUtil.getRoleFromToken(accessToken);
        assertThat(role, is(user.getRole()));
    }
}
