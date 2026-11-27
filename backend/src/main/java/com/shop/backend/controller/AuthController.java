package com.shop.backend.controller;

import com.shop.backend.dto.LoginRequest;
import com.shop.backend.dto.LoginResponse;
import com.shop.backend.entity.User;
import com.shop.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req.getUsername() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body("username and password required");
        }

        Optional<User> userOpt = userRepo.findByUsername(req.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("invalid credentials");
        }

        User user = userOpt.get();
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("invalid credentials");
        }

        // issue token
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userRepo.save(user);

        return ResponseEntity.ok(new LoginResponse(token, user.getId(), user.getUsername()));
    }
}