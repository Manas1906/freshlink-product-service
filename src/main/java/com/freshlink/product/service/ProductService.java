package com.freshlink.product.service;

import com.freshlink.product.model.*;
import com.freshlink.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public Product create(Product p, String email, String role) {
        p.setStatus(ProductStatus.PENDING);
        p.setCreatedBy(email);
        p.setCreatedRole(role);
        return repo.save(p);
    }

    public Product approve(Long id) {
        Product p = repo.findById(id).orElseThrow();
        p.setStatus(ProductStatus.APPROVED);
        return repo.save(p);
    }

    public Product reject(Long id) {
        Product p = repo.findById(id).orElseThrow();
        p.setStatus(ProductStatus.REJECTED);
        return repo.save(p);
    }

    public List<Product> approvedProducts() {
        return repo.findByStatus(ProductStatus.APPROVED);
    }

    public List<Product> getByCategory(Long id) {
        return repo.findByCategoryIdAndStatus(id, ProductStatus.APPROVED);
    }
}
