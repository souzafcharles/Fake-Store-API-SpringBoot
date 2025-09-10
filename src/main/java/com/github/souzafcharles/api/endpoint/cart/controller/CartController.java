package com.github.souzafcharles.api.endpoint.cart.controller;

import com.github.souzafcharles.api.endpoint.cart.model.dto.CartRequestDTO;
import com.github.souzafcharles.api.endpoint.cart.model.dto.CartResponseDTO;
import com.github.souzafcharles.api.endpoint.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CartResponseDTO>>> getAll(
            Pageable pageable,
            PagedResourcesAssembler<CartResponseDTO> assembler
    ) {
        var page = cartService.getAllCarts(pageable);
        var model = assembler.toModel(page, cart ->
                EntityModel.of(cart, WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(CartController.class).getById(cart.id())
                ).withSelfRel())
        );
        return page.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping
    public ResponseEntity<CartResponseDTO> create(@RequestBody @Valid CartRequestDTO dto) {
        CartResponseDTO created = cartService.createCart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponseDTO> update(
            @PathVariable String id,
            @RequestBody @Valid CartRequestDTO dto
    ) {
        return ResponseEntity.ok(cartService.updateCart(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartResponseDTO>> getByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCartsByUserId(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CartResponseDTO>> getByProductId(@PathVariable String productId) {
        return ResponseEntity.ok(cartService.getCartsByProductId(productId));
    }

    @GetMapping("/user/{userId}/total-products")
    public ResponseEntity<Long> getTotalProductsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getTotalProductsForUser(userId));
    }

    @GetMapping("/total-value")
    public ResponseEntity<List<CartResponseDTO>> getCartsWithTotalValueGreaterThan(@RequestParam Double minTotal) {
        return ResponseEntity.ok(cartService.getCartsWithTotalValueGreaterThan(minTotal));
    }
}