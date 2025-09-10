package com.github.souzafcharles.api.endpoint.cartproduct.controller;

import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.CartProductRequestDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.CartProductResponseDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.ProductSalesDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.service.CartProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart-products")
public class CartProductController {

    private final CartProductService cartProductService;

    public CartProductController(CartProductService cartProductService) {
        this.cartProductService = cartProductService;
    }

    @PostMapping("/{cartId}")
    public ResponseEntity<CartProductResponseDTO> addProductToCart(
            @PathVariable String cartId,
            @RequestBody @Valid CartProductRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartProductService.addProductToCart(cartId, dto));
    }

    @DeleteMapping("/{cartId}/{productId}")
    public ResponseEntity<Void> removeProductFromCart(
            @PathVariable String cartId,
            @PathVariable String productId
    ) {
        cartProductService.removeProductFromCart(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartProductResponseDTO>> getProductsInCart(@PathVariable String cartId) {
        return ResponseEntity.ok(cartProductService.getProductsInCart(cartId));
    }

    @GetMapping("/analytics/most-sold")
    public ResponseEntity<List<ProductSalesDTO>> getMostSoldProducts(
            @RequestParam(defaultValue = "5") int topN
    ) {
        return ResponseEntity.ok(cartProductService.getMostSoldProducts(topN));
    }

    @GetMapping("/analytics/revenue")
    public ResponseEntity<Map<String, Double>> getRevenuePerProduct() {
        return ResponseEntity.ok(cartProductService.getRevenuePerProduct());
    }

    @GetMapping("/analytics/total-items")
    public ResponseEntity<Long> getTotalItemsInCarts() {
        return ResponseEntity.ok(cartProductService.getTotalItemsInCarts());
    }

    @GetMapping("/analytics/carts-by-product/{productId}")
    public ResponseEntity<List<String>> getCartsContainingProduct(@PathVariable String productId) {
        return ResponseEntity.ok(cartProductService.getCartsContainingProduct(productId));
    }
}
