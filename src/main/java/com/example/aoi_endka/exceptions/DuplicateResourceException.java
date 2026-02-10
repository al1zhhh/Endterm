package com.example.aoi_endka.exceptions;

/**
 * Exception for duplicate resource errors
 * Extends InvalidInputException
 * Used when trying to create a resource that already exists
 */

public class DuplicateResourceException extends InvalidInputException{
    /**
     * Constructor with message
     * @param message Error message describing the duplicate resource
     */
    public DuplicateResourceException(String message){
        super(message);
    }
    public DuplicateResourceException(String message,Throwable cause){
        super(message,cause);
    }
}
