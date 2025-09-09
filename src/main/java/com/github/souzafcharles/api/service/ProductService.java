package com.github.souzafcharles.api.service;

import com.github.souzafcharles.api.client.FakeStoreClient;
import com.github.souzafcharles.api.model.dto.CartProductDTO;
import com.github.souzafcharles.api.model.dto.ProductRequestDTO;
import com.github.souzafcharles.api.model.dto.ProductResponseDTO;
import com.github.souzafcharles.api.model.entity.Product;
import com.github.souzafcharles.api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final FakeStoreClient fakeStoreClient;

    public ProductService(ProductRepository productRepository, FakeStoreClient fakeStoreClient) {
        this.productRepository = productRepository;
        this.fakeStoreClient = fakeStoreClient;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        return new ProductResponseDTO(product);
    }

    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        Product product = new Product();
        product.setTitle(dto.title());
        product.setPrice(dto.price());
        product.setDescription(dto.description());
        product.setCategory(dto.category());
        product.setImage(dto.image());
        Product saved = productRepository.save(product);
        return new ProductResponseDTO(saved);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        product.setTitle(dto.title());
        product.setPrice(dto.price());
        product.setDescription(dto.description());
        product.setCategory(dto.category());
        product.setImage(dto.image());
        return new ProductResponseDTO(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
        productRepository.delete(product);
    }


    public List<ProductResponseDTO> getTopExpensiveProducts(int topN) {
        return productRepository.findAllByOrderByPriceDesc().stream()
                .limit(topN)
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<ProductResponseDTO> getTopCheapestProducts(int topN) {
        return productRepository.findAllByOrderByPriceAsc().stream()
                .limit(topN)
                .map(ProductResponseDTO::new)
                .toList();
    }

    public Map<String, Double> getAveragePricePerCategory() {
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.averagingDouble(Product::getPrice)
                ));
    }

    public List<ProductResponseDTO> getProductsByPriceRange(Double min, Double max) {
        return productRepository.findByPriceBetween(min, max).stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public List<ProductResponseDTO> searchProducts(String keyword) {
        Set<Product> results = new HashSet<>();
        results.addAll(productRepository.findByTitleContainingIgnoreCase(keyword));
        results.addAll(productRepository.findByDescriptionContainingIgnoreCase(keyword));
        return results.stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public Map<Long, Long> getProductPopularity() {
        Flux<CartProductDTO> productsInCartsFlux = fakeStoreClient.getAllCarts()
                .flatMapMany(Flux::fromArray)
                .flatMap(cart -> Flux.fromIterable(cart.products())); // note aqui

        return productsInCartsFlux
                .groupBy(CartProductDTO::id)
                .flatMap(group -> group.count().map(count -> Map.entry(group.key(), count)))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .block();
    }

}