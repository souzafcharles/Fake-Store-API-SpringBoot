package com.github.souzafcharles.api.endpoint.cartproduct.service;

import com.github.souzafcharles.api.endpoint.cart.model.entity.Cart;
import com.github.souzafcharles.api.endpoint.cart.repository.CartRepository;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.CartProductRequestDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.CartProductResponseDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.ProductSalesDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.entity.CartProduct;
import com.github.souzafcharles.api.endpoint.cartproduct.model.entity.CartProductId;
import com.github.souzafcharles.api.endpoint.product.model.entity.Product;
import com.github.souzafcharles.api.endpoint.product.repository.ProductRepository;
import com.github.souzafcharles.api.exceptions.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartProductService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartProductService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public CartProductResponseDTO addProductToCart(String cartId, CartProductRequestDTO dto) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> ResourceNotFoundException.forCart(cartId));

        Product product = productRepository.findById(dto.productId())
                .orElseThrow(() -> ResourceNotFoundException.forProduct(dto.productId()));

        CartProduct cartProduct = new CartProduct();
        cartProduct.setId(new CartProductId());
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(dto.quantity());

        cart.getCartProducts().add(cartProduct);
        cartRepository.save(cart);

        return new CartProductResponseDTO(cartProduct);
    }

    public void deleteProductFromCart(String cartId, String productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> ResourceNotFoundException.forCart(cartId));

        boolean removed = cart.getCartProducts().removeIf(cp -> cp.getProduct().getId().equals(productId));

        if (!removed) {
            throw ResourceNotFoundException.forProduct(productId);
        }

        cartRepository.save(cart);
    }

    public List<CartProductResponseDTO> getProductsInCart(String cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> ResourceNotFoundException.forCart(cartId));

        return cart.getCartProducts().stream()
                .map(CartProductResponseDTO::new)
                .toList();
    }

    public List<ProductSalesDTO> getMostSoldProducts(int topN) {
        return cartRepository.findAll().stream()
                .flatMap(cart -> cart.getCartProducts().stream())
                .collect(Collectors.groupingBy(
                        cp -> cp.getProduct().getId(),
                        Collectors.summingInt(cp -> cp.getQuantity() != null ? cp.getQuantity() : 0)
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(topN)
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey()).orElse(null);
                    if (product == null) {
                        return null;
                    }
                    return new ProductSalesDTO(product.getId(), product.getTitle(), entry.getValue());
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public Map<String, Double> getRevenuePerProduct() {
        Map<String, Double> revenueMap = new HashMap<>();

        cartRepository.findAll().forEach(cart ->
                cart.getCartProducts().forEach(cp -> {
                    double revenue = cp.getQuantity() * cp.getProduct().getPrice();
                    revenueMap.merge(cp.getProduct().getTitle(), revenue, Double::sum);
                })
        );

        return revenueMap;
    }

    public long getTotalItemsInCarts() {
        return cartRepository.findAll().stream()
                .flatMap(cart -> cart.getCartProducts().stream())
                .mapToLong(CartProduct::getQuantity)
                .sum();
    }

    public List<String> getCartsContainingProduct(String productId) {
        return cartRepository.findAll().stream()
                .filter(cart -> cart.getCartProducts().stream()
                        .anyMatch(cp -> cp.getProduct().getId().equals(productId)))
                .map(Cart::getId)
                .toList();
    }
}
