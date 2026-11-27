package com.shop.backend.controller;

import com.shop.backend.service.CartService;
import com.shop.backend.entity.User;
import com.shop.backend.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductRepository productRepo;

    public CartController(CartService cartService, ProductRepository productRepo) {
        this.cartService = cartService;
        this.productRepo = productRepo;
    }

    private User currentUser(HttpServletRequest req) {
        return (User) req.getAttribute("user");
    }

    @GetMapping
    public Map<String, Object> view(HttpServletRequest req) {
        User user = currentUser(req);
        Map<Long, Integer> cart = cartService.getCart(user.getId());

        List<Map<String, Object>> items = new ArrayList<>();
        double total = 0;
        for (Map.Entry<Long, Integer> e : cart.entrySet()) {
            var productOpt = productRepo.findById(e.getKey());
            if (productOpt.isEmpty()) continue;
            var p = productOpt.get();
            double lineTotal = p.getPrice() * e.getValue();
            total += lineTotal;
            items.add(Map.of(
                "productId", p.getId(),
                "name", p.getName(),
                "price", p.getPrice(),
                "quantity", e.getValue(),
                "lineTotal", lineTotal
            ));
        }
        return Map.of("items", items, "total", total);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Map<String, Object> body, HttpServletRequest req) {
        User user = currentUser(req);
        if (body.get("productId") == null || body.get("quantity") == null) {
            return ResponseEntity.badRequest().body("productId and quantity required");
        }
        Long productId = ((Number) body.get("productId")).longValue();
        int quantity = ((Number) body.get("quantity")).intValue();

        if (productRepo.findById(productId).isEmpty()) {
            return ResponseEntity.status(404).body("product not found");
        }

        try {
            cartService.addItem(user.getId(), productId, quantity);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok(cartService.getCart(user.getId()));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> remove(@PathVariable Long productId, HttpServletRequest req) {
        User user = currentUser(req);
        cartService.removeItem(user.getId(), productId);
        return ResponseEntity.ok(cartService.getCart(user.getId()));
    }
}