package com.github.souzafcharles.api.exceptions.custom;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(Object email) {
        super("The email address '" + email + "' is already associated with an existing account.");
    }
}