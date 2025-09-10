package com.github.souzafcharles.api.product.initializer;

import com.github.souzafcharles.api.product.model.dto.ProductResponseDTO;
import com.github.souzafcharles.api.product.repository.ProductRepository;
import com.github.souzafcharles.api.client.FakeStoreClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class ProductDataInitializerTest {

    private ProductRepository productRepository;
    private FakeStoreClient fakeStoreClient;
    private ProductDataInitializer initializer;

    @BeforeEach
    void setup() {
        productRepository = mock(ProductRepository.class);
        fakeStoreClient = mock(FakeStoreClient.class);
        initializer = new ProductDataInitializer(productRepository, fakeStoreClient);
    }

    @Test
    void init_ShouldSaveProductsWhenRepositoryIsEmpty() {
        when(productRepository.count()).thenReturn(0L);

        ProductResponseDTO dto = new ProductResponseDTO("1", "Laptop", 1500.0, "Gaming", "Electronics", null);
        when(fakeStoreClient.getAllProducts()).thenReturn(Mono.just(new ProductResponseDTO[]{dto}));

        initializer.init();

        verify(productRepository, times(1)).saveAll(anyList());
    }

    @Test
    void init_ShouldNotSaveWhenRepositoryNotEmpty() {
        when(productRepository.count()).thenReturn(5L);

        initializer.init();

        verify(fakeStoreClient, never()).getAllProducts();
        verify(productRepository, never()).saveAll(anyList());
    }
}
