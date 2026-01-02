package com.freshlink.product.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private String createdBy;
    private String createdRole;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;

    @ManyToOne
    private Category category;
}
