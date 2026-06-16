package com.msedcl.main.exception;

public class DbCredentialMissingException extends RuntimeException {

    public DbCredentialMissingException(String message) {
        super(message);
    }
}