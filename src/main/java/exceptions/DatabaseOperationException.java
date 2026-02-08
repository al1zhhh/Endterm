package exceptions;

/**
 * Exception for database operation failures
 * Used when SQL operations fail,connections fail,or business rules are violated
 */
public class DatabaseOperationException extends Exception{

    public DatabaseOperationException(String message){
        super(message);
    }
    public DatabaseOperationException(String message,Throwable cause){
        super(message,cause);
    }
}
