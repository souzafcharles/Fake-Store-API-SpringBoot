package com.github.souzafcharles.api.cart.model.dto;

import com.github.souzafcharles.api.cart.model.entity.Cart;
import com.github.souzafcharles.api.cartproduct.model.dto.CartProductResponseDTO;

import java.io.Serializable;
import java.util.List;

public record CartResponseDTO(
        String id,
        String userId,
        List<CartProductResponseDTO> products
) implements Serializable {
    public CartResponseDTO(Cart cart) {
        this(
                cart.getId(),
                cart.getUser().getId(),
                cart.getCartProducts().stream()
                        .map(CartProductResponseDTO::new)
                        .toList()
        );
    }
}