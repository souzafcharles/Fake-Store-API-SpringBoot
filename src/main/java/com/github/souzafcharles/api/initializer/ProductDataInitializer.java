package com.github.souzafcharles.api.initializer;

import com.github.souzafcharles.api.client.FakeStoreClient;
import com.github.souzafcharles.api.model.dto.ProductResponseDTO;
import com.github.souzafcharles.api.model.entity.Product;
import com.github.souzafcharles.api.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductDataInitializer {

    private final ProductRepository productRepository;
    private final FakeStoreClient fakeStoreClient;

    public ProductDataInitializer(ProductRepository productRepository, FakeStoreClient fakeStoreClient) {
        this.productRepository = productRepository;
        this.fakeStoreClient = fakeStoreClient;
    }

    @PostConstruct
    public void init() {
        if (productRepository.count() == 0) {
            ProductResponseDTO[] products = fakeStoreClient.getAllProducts().block();
            if (products != null) {
                List<Product> entities = Arrays.stream(products)
                        .map(dto -> {
                            Product product = new Product();
                            product.setId(dto.id());
                            product.setTitle(dto.title());
                            product.setPrice(dto.price());
                            product.setDescription(dto.description());
                            product.setCategory(dto.category());
                            product.setImage(dto.image());
                            return product;
                        })
                        .toList();
                productRepository.saveAll(entities);
            }
        }
    }
}