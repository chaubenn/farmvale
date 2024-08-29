package farm.core;

/**
 * A class representing InvalidStockRequestException exception.
 */
public class InvalidStockRequestException extends Exception {
    /**
     * Default signature to call exception.
     **/
    public InvalidStockRequestException() {
        super();
    }

    /**
     * Calls exception, outputting unique message
     **/
    public InvalidStockRequestException(String message) {
        super(message);
    }
}





