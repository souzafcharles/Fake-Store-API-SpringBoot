package com.github.souzafcharles.api.exceptions.custom;

import com.github.souzafcharles.api.utils.Messages;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Object id) {
        super(Messages.PRODUCT_NOT_FOUND + id);
    }
}

