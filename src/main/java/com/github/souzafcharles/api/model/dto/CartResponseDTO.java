package com.github.souzafcharles.api.model.dto;

import java.util.List;

public record CartResponseDTO(
        Long id,
        Long userId,
        List<CartProductDTO> products
) {}
