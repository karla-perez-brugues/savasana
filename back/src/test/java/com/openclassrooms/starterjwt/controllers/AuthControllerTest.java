package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    AuthController authController;

    @BeforeEach
    public void setUp() {
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);
    }

    @Test // FIXME : ClassCastException: String cannot be cast to UserDetailsImpl
    public void authenticateUser_success() {
        Long id = 1L;
        String email = "karla@mail.com";
        String password = "password";
        String firstName = "Karla";
        String lastName = "Pérez";
        boolean admin = true;

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setAdmin(admin);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        JwtResponse jwtResponse = new JwtResponse("token", id, email, firstName, lastName, admin);

        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("token");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<?> result = authController.authenticateUser(loginRequest);

        verify(authenticationManager).authenticate(authentication);
        verify(jwtUtils).generateJwtToken(authentication);
        verify(userRepository).findByEmail(email);

        assertEquals(ResponseEntity.ok(jwtResponse), result);
    }

    @Test
    public void registerUser_success() {
        String email = "karla@mail.com";
        String password = "password";
        String encodedPassword = "p*a*s*s*w*o*r*d";
        String firstName = "Karla";
        String lastName = "Pérez";
        boolean admin = false;

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setFirstName(firstName);
        signupRequest.setLastName(lastName);
        signupRequest.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(encodedPassword);
        user.setAdmin(admin);

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<?> result = authController.registerUser(signupRequest);

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(user);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertInstanceOf(MessageResponse.class, result.getBody());
    }

    @Test
    public void registerUser_badRequest() {
        String email = "karla@mail.com";
        String password = "password";
        String firstName = "Karla";
        String lastName = "Pérez";

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(email);
        signupRequest.setFirstName(firstName);
        signupRequest.setLastName(lastName);
        signupRequest.setPassword(password);

        when(userRepository.existsByEmail(email)).thenReturn(true);

        ResponseEntity<?> result = authController.registerUser(signupRequest);

        verify(userRepository).existsByEmail(email);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertInstanceOf(MessageResponse.class, result.getBody());
    }
}
