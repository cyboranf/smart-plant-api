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

@RestController
@RequestMapping("/api")
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

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
}
