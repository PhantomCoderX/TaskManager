package com.taskmanager.backend.security.jwt;

import com.taskmanager.backend.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    /** Эндпоинты, которые можно вызывать с просроченным access-token’ом */
    private static final Set<String> OPEN_POST_ENDPOINTS = Set.of(
            "/auth/login", "/auth/register", "/auth/refresh-token",
            "/api/auth/login", "/api/auth/register", "/api/auth/refresh-token"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        /* ── 1. login / register / refresh пропускаем без валидации JWT ── */
        if ("POST".equalsIgnoreCase(req.getMethod())
                && OPEN_POST_ENDPOINTS.contains(req.getServletPath())) {
            chain.doFilter(req, res);
            return;
        }

        /* ── 2. Для остальных запросов проверяем Authorization ── */
        String header = req.getHeader("Authorization");

        try {
            if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
                return;
            }

            String token = header.substring(7);
            if (!jwtUtils.validateJwtToken(token)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            String username = jwtUtils.getUserNameFromJwtToken(token);
            var userDetails = userDetailsService.loadUserByUsername(username);

            var auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception ex) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        /* ── 3. Всё ок — продолжаем цепочку ── */
        chain.doFilter(req, res);
    }
}
