package farm.core;

/**
 * A class representing FailedTransactionException exception.
 */
public class FailedTransactionException extends Exception {
    /**
     * Default signature to call exception.
     **/
    public FailedTransactionException() {
        super();
    }

    /**
     * Calls exception, outputting unique message
     **/
    public FailedTransactionException(String message) {
        super(message);
    }
}





