package com.github.souzafcharles.api.client;

import com.github.souzafcharles.api.Product.model.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FakeStoreClientTest {

    private WebClient webClient;
    private FakeStoreClient client;

    @BeforeEach
    void setup() {
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        client = new FakeStoreClient(webClient);
    }

    @Test
    void getAllProducts_ShouldReturnProducts() {
        ProductResponseDTO[] mockProducts = {
                new ProductResponseDTO("1", "Laptop", 1500.0, "Gaming", "Electronics", null)
        };

        when(webClient.get()
                .uri(anyString())
                .retrieve()
                .bodyToMono(ProductResponseDTO[].class))
                .thenReturn(Mono.just(mockProducts));

        ProductResponseDTO[] result = client.getAllProducts().block();

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("Laptop", result[0].title());
    }
}