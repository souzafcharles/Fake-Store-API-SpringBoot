package com.github.souzafcharles.api.exceptions.custom;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super("Database error detected: " + message + ". Please verify database constraints and configurations");
    }
}