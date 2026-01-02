package com.freshlink.product.repository;

import com.freshlink.product.model.Product;
import com.freshlink.product.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByStatus(ProductStatus status);
    List<Product> findByCategoryIdAndStatus(Long id, ProductStatus status);

}
