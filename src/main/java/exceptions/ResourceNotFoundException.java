package exceptions;

/**
 * Exception when a requested resource is not found
 * Used when trying to retrieve,update,or delete a non-existent entity
 */


public class ResourceNotFoundException extends Exception{

    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String message,Throwable cause){
        super(message,cause);

    }
}