package com.shop.backend.controller;

import com.shop.backend.entity.User;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
public class MeController {

    @GetMapping("/me")
    public Map<String, Object> me(HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        return Map.of("userId", user.getId(), "username", user.getUsername());
    }
}