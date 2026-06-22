package com.freshlink.product.service;

import com.freshlink.product.model.*;
import com.freshlink.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import lombok.Data;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final RestTemplate restTemplate = new RestTemplate();

    @org.springframework.beans.factory.annotation.Value("${checkout.service.url:http://localhost:8085}")
    private String checkoutServiceUrl;

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

    @org.springframework.transaction.annotation.Transactional
    public void reduceStock(Long checkoutId) {
        String url = checkoutServiceUrl + "/api/checkout/" + checkoutId + "/items";
        try {
            CheckoutItemDto[] items = restTemplate.getForObject(url, CheckoutItemDto[].class);
            if (items != null) {
                for (CheckoutItemDto item : items) {
                    Product product = repo.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProductId()));
                    if (product.getStock() != null) {
                        if (product.getStock() < item.getQuantity()) {
                            throw new RuntimeException("Insufficient stock for product: " + product.getName());
                        }
                        product.setStock(product.getStock() - item.getQuantity());
                        repo.save(product);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to reduce stock: " + e.getMessage(), e);
        }
    }

    @Data
    public static class CheckoutItemDto {
        private Long productId;
        private Integer quantity;
    }
}
