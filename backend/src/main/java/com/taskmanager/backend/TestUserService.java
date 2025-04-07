//package com.taskmanager.backend;
//
//import com.taskmanager.backend.service.UserService;
//import com.taskmanager.backend.repository.UserRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TestUserService implements CommandLineRunner {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    public TestUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void run(String... args) {
//        System.out.println("==== ТЕСТ ХЭШИРОВАНИЯ ПАРОЛЕЙ ====");
//
//        String email = "test@example.com";
//        String password = "securepassword";
//
//        // Регистрируем пользователя
//        userService.registerUser(email, password);
//        System.out.println("✅ Пользователь зарегистрирован: " + email);
//
//        // Проверяем аутентификацию
//        boolean isAuthenticated = userService.authenticate(email, password);
//        System.out.println("🔐 Аутентификация пройдена? " + isAuthenticated);
//    }
//}
