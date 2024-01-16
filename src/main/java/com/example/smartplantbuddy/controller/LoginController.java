package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.dto.login.LoginRequestDTO;
import com.example.smartplantbuddy.dto.login.LoginResponseDTO;
import com.example.smartplantbuddy.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
/**
 * REST controller for handling user authentication.
 * Provides an endpoint for user login, returning a JWT for successful authentication.
 *
 * @author cyboranf
 * @version 1.0
 * @since 1.0
 */

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }
    /**
     * Authenticates a user based on login credentials.
     * If authentication is successful, a JWT token is generated and returned.
     *
     * @param loginRequestDTO The DTO containing the user's login credentials.
     * @param response The HttpServletResponse used for setting the JWT cookie.
     * @return The response entity containing the JWT and user login.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getLogin(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        Cookie jwtCookie = new Cookie("role", jwt);

        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 4);

        String cookieString = jwtCookie.getName() + "=" + jwtCookie.getValue() + "; SameSite=NoNe";

        response.setHeader("Set-Cookie", cookieString);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwt);
        loginResponseDTO.setLogin(loginRequestDTO.getLogin());

        return ResponseEntity.ok(loginResponseDTO);
    }

    /**
     * Logs out a user by clearing the JWT cookie.
     * This method doesn't invalidate the JWT but removes it from the client-side,
     * preventing its use in future requests.
     *
     * @param response The HttpServletResponse used for clearing the JWT cookie.
     * @return A ResponseEntity indicating the operation's success or failure.
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("role", null); // Set cookie value to null

        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Set cookie expiration to immediate

        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Logged out successfully.");
    }
}
