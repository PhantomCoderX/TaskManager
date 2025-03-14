package com.taskmanager.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Разрешить доступ к эндпоинтам Actuator без аутентификации
                .requestMatchers("/actuator/health").permitAll()
                // Все остальные запросы требуют аутентификации
                .anyRequest().authenticated()
            )
            .httpBasic(); // Использовать базовую аутентификацию

        return http.build();
    }
}