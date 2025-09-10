package com.github.souzafcharles.api.utils;

public class Messages {

    private Messages() {
        throw new IllegalStateException("Utility class");
    }

    // ===== Product =====
    public static final String PRODUCT_TITLE_REQUIRED = "The title is required";
    public static final String PRODUCT_PRICE_REQUIRED = "The price is required";
    public static final String PRODUCT_PRICE_POSITIVE = "The price must be a positive value";
    public static final String PRODUCT_DESCRIPTION_MAX_LENGTH = "The description must be no longer than 5000 characters";
    public static final String PRODUCT_CATEGORY_REQUIRED = "The category is required";
    public static final String PRODUCT_NOT_FOUND = "Product not found with ID: {}";
    public static final String PRODUCT_ALREADY_INITIALIZED = "Products already initialized. Skipping load.";
    public static final String PRODUCT_NO_RETURNED = "No product returned from the Fake Store API.";
    public static final String PRODUCT_SAVED_SUCCESS = "{} products were saved to the H2 database.";

    // ===== User =====
    public static final String USERNAME_REQUIRED = "The username is required";
    public static final String EMAIL_REQUIRED = "The email is required";
    public static final String EMAIL_INVALID = "The email is invalid";
    public static final String PASSWORD_REQUIRED = "The password is required";
    public static final String PASSWORD_MIN_LENGTH = "The password must be at least 6 characters long";
    public static final String USER_NOT_FOUND = "User not found with ID: {}";
    public static final String USER_ALREADY_INITIALIZED = "Users already initialized. Skipping load.";
    public static final String USER_NO_RETURNED = "No users returned from the Fake Store API.";
    public static final String USER_SAVED_SUCCESS = "{} users were saved to the H2 database.";

    // ===== Cart =====
    public static final String CART_USER_REQUIRED = "The user is required for the cart";
    public static final String CART_PRODUCTS_REQUIRED = "The products list is required for the cart";
    public static final String CART_PRODUCT_QUANTITY_POSITIVE = "The quantity must be greater than zero";
    public static final String CART_NOT_FOUND = "Cart not found with ID: {}";
    public static final String CART_ALREADY_INITIALIZED = "Carts already initialized. Skipping load.";
    public static final String CART_NO_RETURNED = "No cart returned from the Fake Store API.";
    public static final String CART_PRODUCT_IGNORED = "Ignoring CartProduct in Cart ID {}: Product ID {} not found.";
    public static final String CART_IGNORED_USER_NOT_FOUND = "Ignoring Cart ID {}: User ID {} not found.";
    public static final String CART_SAVED_SUCCESS = "{} carts were saved to the H2 database.";
    public static final String CART_NO_VALID = "No valid cart to save.";

}
