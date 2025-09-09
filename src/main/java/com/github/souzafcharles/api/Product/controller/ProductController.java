package com.github.souzafcharles.api.Product.controller;

import com.github.souzafcharles.api.Product.model.dto.ProductRequestDTO;
import com.github.souzafcharles.api.Product.model.dto.ProductResponseDTO;
import com.github.souzafcharles.api.Product.service.ProductService;
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
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO dto) {
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable String id, @RequestBody @Valid ProductRequestDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponseDTO>>> getAll(
            Pageable pageable,
            PagedResourcesAssembler<ProductResponseDTO> assembler
    ) {
        var page = productService.getAllProducts(pageable);
        var model = assembler.toModel(page, product ->
                EntityModel.of(product,
                        WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(ProductController.class).getById(product.id())
                        ).withSelfRel()
                )
        );
        return page.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(model);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDTO>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @GetMapping("/top-expensive")
    public ResponseEntity<List<ProductResponseDTO>> topExpensive(@RequestParam(defaultValue = "5") int topN) {
        return ResponseEntity.ok(productService.getTopExpensiveProducts(topN));
    }

    @GetMapping("/top-cheapest")
    public ResponseEntity<List<ProductResponseDTO>> topCheapest(@RequestParam(defaultValue = "5") int topN) {
        return ResponseEntity.ok(productService.getTopCheapestProducts(topN));
    }

    @GetMapping("/average-price-category")
    public ResponseEntity<Map<String, Double>> averagePricePerCategory() {
        return ResponseEntity.ok(productService.getAveragePricePerCategory());
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDTO>> productsByPriceRange(@RequestParam Double min, @RequestParam Double max) {
        return ResponseEntity.ok(productService.getProductsByPriceRange(min, max));
    }
}
