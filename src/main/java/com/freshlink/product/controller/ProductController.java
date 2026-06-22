package com.freshlink.product.controller;

import com.freshlink.product.model.Product;
import com.freshlink.product.model.ProductStatus;
import com.freshlink.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    public Product create(@RequestBody Product p, Authentication auth) {
        String role = "USER";
        String email = "anonymous";
        if (auth != null) {
            email = auth.getName();
            if (auth.getAuthorities() != null && !auth.getAuthorities().isEmpty()) {
                role = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            }
        }
        return service.create(p, email, role);
    }

    @PostMapping("/reduce-stock/{checkoutId}")
    public void reduceStock(@PathVariable Long checkoutId) {
        service.reduceStock(checkoutId);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product approve(@PathVariable Long id) {
        return service.approve(id);
    }

    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Product reject(@PathVariable Long id) {
        return service.reject(id);
    }

    @GetMapping
    public List<Product> all() {
        return service.approvedProducts();
    }

    @GetMapping("/category/{id}")
    public List<Product> byCategory(@PathVariable Long id) {
        return service.getByCategory(id);
    }
}
