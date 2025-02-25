package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "jwtSecret");
    }

    @Test
    public void testGenerateJwtToken() {
        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = UserDetailsImpl.builder().username("karla@mail.com").build();

        when(authentication.getPrincipal()).thenReturn(userDetails);

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testGetUserNameFromJwtToken() {
        String token = Jwts.builder()
                .setSubject("karla@mail.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 60000))
                .signWith(SignatureAlgorithm.HS512, "jwtSecret")
                .compact();

        String userName = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals("karla@mail.com", userName);
    }

    @Test
    public void testValidateJwtToken() {
        String token = Jwts.builder()
                .setSubject("karla@mail.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 60000))
                .signWith(SignatureAlgorithm.HS512, "jwtSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    public void testValidateJwtToken_SignatureException() {
        String token = Jwts.builder()
                .setSubject("karla@mail.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 60000))
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_MalformedJwtException() {
        String token = "token";

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_ExpiredJwtException() throws InterruptedException {
        String token = Jwts.builder()
                .setSubject("karla@mail.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 100))
                .signWith(SignatureAlgorithm.HS512, "jwtSecret")
                .compact();

        Thread.sleep(200);

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void testValidateJwtToken_UnsupportedJwtException() {
        String token = Jwts.builder()
                .setSubject("karla@mail.com")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

}
