// AuthController.java
package com.taskmanager.backend.controller.auth;

import com.taskmanager.backend.dto.*;
import com.taskmanager.backend.security.jwt.JwtUtils;
import com.taskmanager.backend.security.service.*;
import com.taskmanager.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserService userService;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private RefreshTokenService refreshTokenService;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    // LOGIN → access + refresh (persisted)
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsImpl ud = (UserDetailsImpl) auth.getPrincipal();

        String accessToken  = jwtUtils.generateJwtToken(ud.getUsername());
        // сохраняем и возвращаем refresh
        var refreshToken = refreshTokenService.createRefreshToken(ud.getId());

        return ResponseEntity.ok(new JwtResponse(
                accessToken,
                refreshToken.getToken(),
                ud.getId(),
                ud.getName(),
                ud.getEmail()
        ));
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequest req) {
        try {
            userService.registerUser(req.getUsername(), req.getEmail(), req.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.changePassword(email, dto.getOldPass(), dto.getNewPass());
        return ResponseEntity.ok("Password changed successfully");
    }

    // REFRESH ACCESS TOKEN
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String rt = request.refreshToken();
        return refreshTokenService.findByToken(rt)
                .map(refreshTokenService::verifyExpiration)
                .map(validToken -> {
                    String username = validToken.getUser().getEmail();
                    String newAccess = jwtUtils.generateJwtToken(username);
                    return ResponseEntity.ok(new TokenRefreshResponse(newAccess, validToken.getToken()));
                })
                .orElseGet(() ->
                        ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                // Возвращаем «пустой» объект и ошибочный статус
                                .body(new TokenRefreshResponse("", ""))
                );
    }
}
