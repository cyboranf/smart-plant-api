package com.example.smartplantbuddy.controller;

import com.example.smartplantbuddy.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/isUser")
    public ResponseEntity<?> isUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);

            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            boolean isUserOrGuest = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            "ROLE_USER".equals(grantedAuthority.getAuthority()));
            if (isUserOrGuest) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Welcome back, " + username);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }
}
