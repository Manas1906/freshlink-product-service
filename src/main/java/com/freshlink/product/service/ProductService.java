package com.freshlink.product.service;

import com.freshlink.product.model.*;
import com.freshlink.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public Product create(Product p) {
        Category cat = categoryRepo.findByName(p.getCategory().getName())
                .orElseGet(() -> categoryRepo.save(p.getCategory()));
        p.setCategory(cat);
        return productRepo.save(p);
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public List<Product> getByCategory(Long categoryId) {
        return productRepo.findByCategoryId(categoryId);
    }
}
