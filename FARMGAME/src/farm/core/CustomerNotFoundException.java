package farm.core;

/**
 * A class representing CustomerNotFoundException exception.
 */
public class CustomerNotFoundException extends Exception {
    /**
     * Default signature to call exception.
     **/
    public CustomerNotFoundException() {
        super();
    }

    /**
     * Calls exception, outputting unique message
     **/
    public CustomerNotFoundException(String message) {
        super(message);
    }
}





