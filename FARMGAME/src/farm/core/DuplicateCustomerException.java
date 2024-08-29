package farm.core;

/**
 * A class representing DuplicateCustomerException exception.
 */
public class DuplicateCustomerException extends Exception {
    /**
     * Default signature to call exception.
     **/
    public DuplicateCustomerException() {
        super();
    }

    /**
     * Calls exception, outputting unique message
     **/
    public DuplicateCustomerException(String message) {
        super(message);
    }
}





