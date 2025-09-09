package com.github.souzafcharles.api.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

public record ProductRequestDTO(
        @NotBlank(message = "The title is required") String title,
        @NotNull(message = "The price is required")
        @Positive(message = "The price must be a positive value") Double price,
        @Size(max = 5000, message = "The description must be no longer than 5000 characters") String description,
        @NotBlank(message = "The category is required") String category,
        String image
) implements Serializable { }