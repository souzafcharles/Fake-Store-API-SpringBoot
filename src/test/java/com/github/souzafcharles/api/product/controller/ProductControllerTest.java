package com.github.souzafcharles.api.product.controller;

import com.github.souzafcharles.api.product.model.dto.ProductRequestDTO;
import com.github.souzafcharles.api.product.model.dto.ProductResponseDTO;
import com.github.souzafcharles.api.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private PagedResourcesAssembler<ProductResponseDTO> assembler;

    @InjectMocks
    private ProductController productController;

    private ProductResponseDTO productDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDTO = new ProductResponseDTO("1", "Laptop", 1500.0, "Gaming laptop", "Electronics", null);
    }

    @Test
    void getAll_ShouldReturnNoContentWhenPageIsEmpty() {
        Page<ProductResponseDTO> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(emptyPage);

        ResponseEntity<PagedModel<EntityModel<ProductResponseDTO>>> response =
                productController.getAll(Pageable.unpaged(), assembler);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAll_ShouldReturnPagedModelWhenPageHasData() {
        Page<ProductResponseDTO> page = new PageImpl<>(List.of(productDTO));
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        PagedModel<EntityModel<ProductResponseDTO>> pagedModel = PagedModel.of(List.of(EntityModel.of(productDTO)), new PagedModel.PageMetadata(1, 0, 1));

        when(assembler.toModel(eq(page), ArgumentMatchers.<org.springframework.hateoas.server.RepresentationModelAssembler<ProductResponseDTO, EntityModel<ProductResponseDTO>>>any())).thenReturn(pagedModel);

        ResponseEntity<PagedModel<EntityModel<ProductResponseDTO>>> response = productController.getAll(Pageable.unpaged(), assembler);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    void getByIdShouldReturnProduct() {
        when(productService.getProductById("1")).thenReturn(productDTO);

        ResponseEntity<ProductResponseDTO> response = productController.getById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Laptop", response.getBody().title());
    }

    @Test
    void createShouldReturnCreatedProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO("Phone", 800.0, "Smartphone", "Electronics", null);
        ProductResponseDTO createdDTO = new ProductResponseDTO("2", "Phone", 800.0, "Smartphone", "Electronics", null);

        when(productService.createProduct(requestDTO)).thenReturn(createdDTO);

        ResponseEntity<ProductResponseDTO> response = productController.create(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Phone", response.getBody().title());
    }

    @Test
    void updateShouldReturnUpdatedProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO("Laptop Pro", 1800.0, "Updated", "Electronics", null);
        ProductResponseDTO updatedDTO = new ProductResponseDTO("1", "Laptop Pro", 1800.0, "Updated", "Electronics", null);

        when(productService.updateProduct("1", requestDTO)).thenReturn(updatedDTO);

        ResponseEntity<ProductResponseDTO> response = productController.update("1", requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Laptop Pro", response.getBody().title());
    }

    @Test
    void deleteShouldReturnNoContent() {
        doNothing().when(productService).deleteProduct("1");

        ResponseEntity<Void> response = productController.delete("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct("1");
    }

    @Test
    void searchShouldReturnProducts() {
        when(productService.searchProducts("Laptop")).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductResponseDTO>> response = productController.search("Laptop");

        assertEquals(1, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).title());
    }

    @Test
    void topExpensiveShouldReturnTopProducts() {
        when(productService.getTopExpensiveProducts(5)).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductResponseDTO>> response = productController.topExpensive(5);

        assertEquals(1, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).title());
    }

    @Test
    void topCheapestShouldReturnTopProducts() {
        when(productService.getTopCheapestProducts(5)).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductResponseDTO>> response = productController.topCheapest(5);

        assertEquals(1, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).title());
    }

    @Test
    void averagePricePerCategoryShouldReturnMap() {
        when(productService.getAveragePricePerCategory()).thenReturn(Map.of("Electronics", 1500.0));

        ResponseEntity<Map<String, Double>> response = productController.averagePricePerCategory();

        assertEquals(1500.0, response.getBody().get("Electronics"));
    }

    @Test
    void productsByPriceRangeShouldReturnProducts() {
        when(productService.getProductsByPriceRange(100.0, 2000.0)).thenReturn(List.of(productDTO));

        ResponseEntity<List<ProductResponseDTO>> response = productController.productsByPriceRange(100.0, 2000.0);

        assertEquals(1, response.getBody().size());
        assertEquals("Laptop", response.getBody().get(0).title());
    }

    @Test
    void getAllShouldCoverAssemblerLambda() {

        Page<ProductResponseDTO> page = new PageImpl<>(List.of(productDTO));
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(page);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<org.springframework.hateoas.server.RepresentationModelAssembler<ProductResponseDTO, EntityModel<ProductResponseDTO>>> captor =
                ArgumentCaptor.forClass(org.springframework.hateoas.server.RepresentationModelAssembler.class);

        PagedModel<EntityModel<ProductResponseDTO>> pagedModel = PagedModel.of(List.of(EntityModel.of(productDTO)), new PagedModel.PageMetadata(1, 0, 1));

        when(assembler.toModel(eq(page), captor.capture())).thenReturn(pagedModel);

        ResponseEntity<PagedModel<EntityModel<ProductResponseDTO>>> response = productController.getAll(Pageable.unpaged(), assembler);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());

        var lambda = captor.getValue();
        EntityModel<ProductResponseDTO> entity = lambda.toModel(productDTO);
        assertTrue(entity.hasLink("self"));
    }
}