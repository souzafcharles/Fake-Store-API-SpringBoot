package com.github.souzafcharles.api.utils;

public class Messages {

    private Messages() {
        throw new IllegalStateException("Utility class");
    }

    // Validations for ProductRequestDTO
    public static final String PRODUCT_TITLE_REQUIRED = "The title is required";
    public static final String PRODUCT_PRICE_REQUIRED = "The price is required";
    public static final String PRODUCT_PRICE_POSITIVE = "The price must be a positive value";
    public static final String PRODUCT_DESCRIPTION_MAX_LENGTH = "The description must be no longer than 5000 characters";
    public static final String PRODUCT_CATEGORY_REQUIRED = "The category is required";

    // Exception messages
    public static final String PRODUCT_NOT_FOUND = "Product not found with ID: ";
}

