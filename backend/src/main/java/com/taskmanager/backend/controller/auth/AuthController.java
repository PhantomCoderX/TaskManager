package com.taskmanager.backend.controller.auth;

import com.taskmanager.backend.security.jwt.JwtUtils;
import com.taskmanager.backend.security.service.UserDetailsImpl;
import com.taskmanager.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(
            @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());

        return ResponseEntity.ok(
                new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getName(),
                        userDetails.getEmail())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestBody SignupRequest signUpRequest) {
        userService.registerUser(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword()
        );
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam String oldPass,
            @RequestParam String newPass) {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        userService.changePassword(email, oldPass, newPass);
        return ResponseEntity.ok("Password changed successfully");
    }
}
