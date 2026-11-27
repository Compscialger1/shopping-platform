package com.shop.backend.controller;

import com.shop.backend.entity.Product;
import com.shop.backend.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogController {

    private final ProductRepository productRepo;

    public CatalogController(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/catalog")
    public List<Product> catalog() {
        return productRepo.findAll();
    }
}