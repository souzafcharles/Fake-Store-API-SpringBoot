package com.github.souzafcharles.api.model.dto;

import com.github.souzafcharles.api.model.entity.Product;
import java.io.Serializable;

public record ProductResponseDTO(
        Long id,
        String title,
        Double price,
        String description,
        String category,
        String image
) implements Serializable {
    public ProductResponseDTO(Product product) {
        this(
                product.getId(),
                product.getTitle(),
                product.getPrice(),
                product.getDescription(),
                product.getCategory(),
                product.getImage()
        );
    }
}