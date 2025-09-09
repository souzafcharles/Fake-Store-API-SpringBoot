package com.github.souzafcharles.api.repository;

import com.github.souzafcharles.api.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByTitleContainingIgnoreCase(String title);
    List<Product> findByCategoryIgnoreCase(String category);
    List<Product> findAllByOrderByPriceDesc();
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    List<Product> findByDescriptionContainingIgnoreCase(String keyword);
}
