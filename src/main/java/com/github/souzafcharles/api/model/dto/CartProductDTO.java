package com.github.souzafcharles.api.model.dto;

public record CartProductDTO(
        Long id,
        String title,
        Double price,
        String description,
        String category,
        String image
) {}
