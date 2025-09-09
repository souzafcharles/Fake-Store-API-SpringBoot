package com.github.souzafcharles.api.client;

import com.github.souzafcharles.api.model.dto.ProductResponseDTO;
import com.github.souzafcharles.api.model.dto.CartResponseDTO; // criar DTO para Cart
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FakeStoreClient {

    private final WebClient webClient;

    public FakeStoreClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<ProductResponseDTO[]> getAllProducts() {
        return webClient.get()
                .uri("https://fakestoreapi.com/products")
                .retrieve()
                .bodyToMono(ProductResponseDTO[].class);
    }

    public Mono<ProductResponseDTO> getProductById(Long id) {
        return webClient.get()
                .uri("https://fakestoreapi.com/products/{id}", id)
                .retrieve()
                .bodyToMono(ProductResponseDTO.class);
    }

    public Mono<CartResponseDTO[]> getAllCarts() {
        return webClient.get()
                .uri("https://fakestoreapi.com/carts")
                .retrieve()
                .bodyToMono(CartResponseDTO[].class);
    }
}
