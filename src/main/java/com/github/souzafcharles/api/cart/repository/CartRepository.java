package com.github.souzafcharles.api.cart.repository;

import com.github.souzafcharles.api.cart.model.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    List<Cart> findByUserId(String userId);
    List<Cart> findByCartProductsProductId(String productId);

    default long countTotalProductsForUser(String userId) {
        return findByUserId(userId).stream()
                .flatMap(cart -> cart.getCartProducts().stream())
                .mapToLong(cp -> cp.getQuantity() != null ? cp.getQuantity() : 0)
                .sum();
    }

    default List<Cart> findByTotalValueGreaterThan(Double minTotal) {
        return findAll().stream()
                .filter(cart -> cart.getCartProducts().stream()
                        .mapToDouble(cp -> cp.getQuantity() * cp.getProduct().getPrice())
                        .sum() > minTotal)
                .toList();
    }
}
