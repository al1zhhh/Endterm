package com.example.aoi_endka.exceptions;

/**
 * Base exception for invalid input validation errors
 * Used when user input or data doesnt meet validation requirements
 */


public class InvalidInputException extends Exception{
    /**
     * Constructor with message
     * @param message Error message describing what validation failed
     */
    public InvalidInputException(String message){
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message Error message
     * @param cause The underlying cause of the exception
     */
    public InvalidInputException(String message,Throwable cause){
        super(message,cause);
    }

}
