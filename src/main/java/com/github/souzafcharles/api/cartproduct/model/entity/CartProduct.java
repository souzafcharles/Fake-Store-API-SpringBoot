package com.github.souzafcharles.api.cartproduct.model.entity;

import com.github.souzafcharles.api.cart.model.entity.Cart;
import com.github.souzafcharles.api.product.model.entity.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_cart_product")
public class CartProduct {

    @EmbeddedId
    private CartProductId id = new CartProductId();

    @ManyToOne
    @MapsId("cartId")
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public CartProductId getId() {
        return id;
    }

    public void setId(CartProductId id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
