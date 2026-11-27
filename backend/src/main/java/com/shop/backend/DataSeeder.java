package com.shop.backend;

import com.shop.backend.entity.Product;
import com.shop.backend.entity.User;
import com.shop.backend.repository.ProductRepository;
import com.shop.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public DataSeeder(ProductRepository productRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) {
        if (productRepo.count() == 0) {
            String[][] products = {
                {"Laptop", "999.99", "10"},
                {"Phone", "599.99", "25"},
                {"Headphones", "149.99", "50"},
                {"Keyboard", "89.99", "30"},
                {"Mouse", "39.99", "100"}
            };
            for (String[] p : products) {
                Product product = new Product();
                product.setName(p[0]);
                product.setPrice(Double.parseDouble(p[1]));
                product.setStock(Integer.parseInt(p[2]));
                productRepo.save(product);
            }
        }
if (userRepo.count() == 0) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    User user = new User();
    user.setUsername("alice");
    user.setPassword(encoder.encode("alice123"));
    userRepo.save(user);
}
    }
}