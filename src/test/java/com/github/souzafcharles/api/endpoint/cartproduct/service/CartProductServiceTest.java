package com.github.souzafcharles.api.endpoint.cartproduct.service;

import com.github.souzafcharles.api.endpoint.cart.model.entity.Cart;
import com.github.souzafcharles.api.endpoint.cart.repository.CartRepository;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.CartProductRequestDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.CartProductResponseDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.dto.ProductSalesDTO;
import com.github.souzafcharles.api.endpoint.cartproduct.model.entity.CartProduct;
import com.github.souzafcharles.api.endpoint.product.model.entity.Product;
import com.github.souzafcharles.api.endpoint.product.repository.ProductRepository;
import com.github.souzafcharles.api.exceptions.custom.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartProductServiceTest {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartProductService cartProductService;

    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        productRepository = mock(ProductRepository.class);
        cartProductService = new CartProductService(cartRepository, productRepository);

        cart = new Cart();
        cart.setId("c1");
        cart.setCartProducts(new ArrayList<>());

        product = new Product();
        product.setId("p1");
        product.setTitle("Laptop");
        product.setPrice(1500.0);
    }

    @Test
    void addProductToCartShouldAddNewProduct() {
        // Arrange
        CartProductRequestDTO requestDTO = new CartProductRequestDTO("p1", 2);
        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        // Act
        CartProductResponseDTO response = cartProductService.addProductToCart("c1", requestDTO);

        // Assert
        assertEquals("p1", response.productId());
        assertEquals(2, response.quantity());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void addProductToCartShouldIncreaseQuantityIfExists() {
        // Arrange
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
        cartProduct.setQuantity(1);
        cart.getCartProducts().add(cartProduct);

        CartProductRequestDTO requestDTO = new CartProductRequestDTO("p1", 3);
        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        // Act
        CartProductResponseDTO response = cartProductService.addProductToCart("c1", requestDTO);

        // Assert
        assertEquals(4, response.quantity());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void addProductToCartShouldThrowWhenCartNotFound() {
        // Arrange
        when(cartRepository.findById("c1")).thenReturn(Optional.empty());
        CartProductRequestDTO requestDTO = new CartProductRequestDTO("p1", 1);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cartProductService.addProductToCart("c1", requestDTO));
    }

    @Test
    void addProductToCartShouldThrowWhenProductNotFound() {
        // Arrange
        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));
        when(productRepository.findById("p1")).thenReturn(Optional.empty());
        CartProductRequestDTO requestDTO = new CartProductRequestDTO("p1", 1);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cartProductService.addProductToCart("c1", requestDTO));
    }

    @Test
    void deleteProductFromCartShouldRemoveProduct() {
        // Arrange
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
        cartProduct.setQuantity(1);
        cart.getCartProducts().add(cartProduct);

        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));

        // Act
        cartProductService.deleteProductFromCart("c1", "p1");

        // Assert
        assertTrue(cart.getCartProducts().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void deleteProductFromCartShouldThrowIfProductNotFound() {
        // Arrange
        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cartProductService.deleteProductFromCart("c1", "p1"));
    }

    @Test
    void getProductsInCartShouldReturnList() {
        // Arrange
        CartProduct cartProduct = new CartProduct();
        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity(2);
        cart.getCartProducts().add(cartProduct);

        when(cartRepository.findById("c1")).thenReturn(Optional.of(cart));

        // Act
        List<CartProductResponseDTO> result = cartProductService.getProductsInCart("c1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("p1", result.get(0).productId());
    }

    @Test
    void getMostSoldProductsShouldReturnTopProducts() {
        // Arrange
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
        cartProduct.setQuantity(5);
        cart.getCartProducts().add(cartProduct);
        when(cartRepository.findAll()).thenReturn(List.of(cart));
        when(productRepository.findById("p1")).thenReturn(Optional.of(product));

        // Act
        List<ProductSalesDTO> result = cartProductService.getMostSoldProducts(1);

        // Assert
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).totalSold());
    }

    @Test
    void getRevenuePerProductShouldReturnMap() {
        // Arrange
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
        cartProduct.setQuantity(2);
        cart.getCartProducts().add(cartProduct);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        // Act
        Map<String, Double> revenue = cartProductService.getRevenuePerProduct();

        // Assert
        assertEquals(1, revenue.size());
        assertEquals(3000.0, revenue.get("Laptop"));
    }

    @Test
    void getTotalItemsInCartsShouldReturnSum() {
        // Arrange
        CartProduct cartProduct1 = new CartProduct();
        cartProduct1.setProduct(product);
        cartProduct1.setCart(cart);
        cartProduct1.setQuantity(3);
        cart.getCartProducts().add(cartProduct1);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        // Act
        long total = cartProductService.getTotalItemsInCarts();

        // Assert
        assertEquals(3, total);
    }

    @Test
    void getCartsContainingProductShouldReturnCartIds() {
        // Arrange
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
        cartProduct.setQuantity(1);
        cart.getCartProducts().add(cartProduct);
        when(cartRepository.findAll()).thenReturn(List.of(cart));

        // Act
        List<String> result = cartProductService.getCartsContainingProduct("p1");

        // Assert
        assertEquals(1, result.size());
        assertEquals("c1", result.get(0));
    }
}