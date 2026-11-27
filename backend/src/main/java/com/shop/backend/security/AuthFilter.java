package com.shop.backend.security;

import com.shop.backend.entity.User;
import com.shop.backend.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthFilter implements Filter {

    private final UserRepository userRepo;

    // paths that do NOT require auth
    private static final Set<String> PUBLIC = Set.of("/health", "/catalog", "/auth/login");

    public AuthFilter(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        if (PUBLIC.contains(path) || path.startsWith("/h2")) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("{\"error\":\"missing token\"}");
            return;
        }

        String token = authHeader.substring(7);
        Optional<User> userOpt = userRepo.findByToken(token);
        if (userOpt.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("{\"error\":\"invalid token\"}");
            return;
        }

        request.setAttribute("user", userOpt.get());
        chain.doFilter(req, res);
    }
}