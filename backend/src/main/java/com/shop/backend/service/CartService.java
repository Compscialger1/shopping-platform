package com.shop.backend.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    private final Map<Long, Map<Long, Integer>> carts = new ConcurrentHashMap<>();

    public Map<Long, Integer> getCart(Long userId) {
        return carts.computeIfAbsent(userId, k -> new LinkedHashMap<>());
    }

    public void addItem(Long userId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be positive");
        getCart(userId).merge(productId, quantity, Integer::sum);
    }

    public void removeItem(Long userId, Long productId) {
        getCart(userId).remove(productId);
    }

    public void clear(Long userId) {
        carts.remove(userId);
    }
}