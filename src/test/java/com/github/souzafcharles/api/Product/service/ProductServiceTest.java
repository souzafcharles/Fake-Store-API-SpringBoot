package com.github.souzafcharles.api.Product.service;

import com.github.souzafcharles.api.Product.model.dto.ProductRequestDTO;
import com.github.souzafcharles.api.Product.model.entity.Product;
import com.github.souzafcharles.api.Product.repository.ProductRepository;
import com.github.souzafcharles.api.exceptions.custom.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Product anotherProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId("1");
        product.setTitle("Laptop");
        product.setPrice(1500.0);
        product.setCategory("Electronics");
        product.setDescription("Gaming laptop");

        anotherProduct = new Product();
        anotherProduct.setId("2");
        anotherProduct.setTitle("Chair");
        anotherProduct.setPrice(200.0);
        anotherProduct.setCategory("Furniture");
        anotherProduct.setDescription("Office chair");
    }

    @Test
    void getAllProducts_ShouldReturnPagedProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product, anotherProduct));

        var page = productService.getAllProducts(PageRequest.of(0, 10));
        assertEquals(2, page.getTotalElements());
        assertEquals("Laptop", page.getContent().get(0).title());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        var response = productService.getProductById("1");
        assertEquals("Laptop", response.title());
    }

    @Test
    void getProductById_ShouldThrowException() {
        when(productRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById("99"));
    }

    @Test
    void createProduct_ShouldSaveProduct() {
        ProductRequestDTO dto = new ProductRequestDTO("Phone", 800.0, "Smartphone", "Electronics", null);
        Product savedProduct = new Product();
        savedProduct.setId("3");
        savedProduct.setTitle(dto.title());
        savedProduct.setPrice(dto.price());
        savedProduct.setCategory(dto.category());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        var response = productService.createProduct(dto);
        assertEquals("Phone", response.title());
        assertEquals("3", response.id());
    }

    @Test
    void updateProduct_ShouldUpdateProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductRequestDTO dto = new ProductRequestDTO("Laptop Pro", 1800.0, "Updated", "Electronics", null);
        var response = productService.updateProduct("1", dto);

        assertEquals("Laptop Pro", response.title());
        assertEquals(1800.0, response.price());
    }

    @Test
    void updateProduct_ShouldThrowException() {
        when(productRepository.findById("99")).thenReturn(Optional.empty());
        ProductRequestDTO dto = new ProductRequestDTO("NonExistent", 0.0, "", "None", null);
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct("99", dto));
    }

    @Test
    void deleteProduct_ShouldRemoveProduct() {
        when(productRepository.findById("1")).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(product);

        assertDoesNotThrow(() -> productService.deleteProduct("1"));
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void deleteProduct_ShouldThrowException() {
        when(productRepository.findById("99")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct("99"));
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        when(productRepository.findByTitleContainingIgnoreCase("Laptop")).thenReturn(List.of(product));
        when(productRepository.findByDescriptionContainingIgnoreCase("Laptop")).thenReturn(List.of(product));

        var results = productService.searchProducts("Laptop");
        assertEquals(1, results.size());
        assertEquals("Laptop", results.get(0).title());
    }

    @Test
    void getTopExpensiveProducts_ShouldReturnTopN() {
        when(productRepository.findAllByOrderByPriceDesc()).thenReturn(List.of(product, anotherProduct));
        var top = productService.getTopExpensiveProducts(1);
        assertEquals(1, top.size());
        assertEquals("Laptop", top.get(0).title());
    }

    @Test
    void getTopCheapestProducts_ShouldReturnTopN() {
        when(productRepository.findAllByOrderByPriceAsc()).thenReturn(List.of(anotherProduct, product));
        var top = productService.getTopCheapestProducts(1);
        assertEquals(1, top.size());
        assertEquals("Chair", top.get(0).title());
    }

    @Test
    void getAveragePricePerCategory_ShouldReturnMap() {
        when(productRepository.findAll()).thenReturn(List.of(product, anotherProduct));
        Map<String, Double> averages = productService.getAveragePricePerCategory();
        assertEquals(2, averages.size());
        assertEquals(1500.0, averages.get("Electronics"));
        assertEquals(200.0, averages.get("Furniture"));
    }

    @Test
    void getProductsByPriceRange_ShouldReturnFiltered() {
        when(productRepository.findByPriceBetween(100.0, 1000.0)).thenReturn(List.of(anotherProduct));
        var results = productService.getProductsByPriceRange(100.0, 1000.0);
        assertEquals(1, results.size());
        assertEquals("Chair", results.get(0).title());
    }
}