package farm.sales;

import farm.inventory.product.Product;
import farm.sales.transaction.Transaction;
import farm.core.FailedTransactionException;

/**
 * The controlling class for all transactions. Opens and closes transactions,
 * as well as ensuring only one transaction is active at any given time.
 */
public class TransactionManager {
    /**
     * Assigns variable to ongoing transaction.
     */
    private Transaction ongoingTransaction;

    /**
     * Constructs a TransactionManager with no ongoing transactions.
     */
    public TransactionManager() {
        this.ongoingTransaction = null;
    }

    /**
     * Determine whether a transaction is currently in progress.
     */
    public boolean hasOngoingTransaction() {
        return ongoingTransaction != null;
    }

    /**
     * Begins managing the specified transaction, provided one is not already ongoing.
     */
    public void setOngoingTransaction(Transaction transaction) throws FailedTransactionException {
        if (hasOngoingTransaction()) {
            throw new FailedTransactionException("A transaction is already in progress.");
        }
        this.ongoingTransaction = transaction;
    }

    /**
     * Adds the given product to the cart of the customer associated with the current transaction.
     */
    public void registerPendingPurchase(Product product) throws FailedTransactionException {
        if (!hasOngoingTransaction()) {
            throw new FailedTransactionException("No ongoing transaction to register purchase.");
        }
        if (ongoingTransaction.isFinalised()) {
            throw new FailedTransactionException("The ongoing transaction "
                    + "has already been finalised.");
        }
        ongoingTransaction.getAssociatedCustomer().getCart().addProduct(product);
    }

    /**
     * Finalises the currently ongoing transaction and makes readies the
     * TransactionManager to accept a new ongoing transaction.
     */
    public Transaction closeCurrentTransaction() throws FailedTransactionException {
        if (!hasOngoingTransaction()) {
            throw new FailedTransactionException("No ongoing transaction to close.");
        }
        ongoingTransaction.finalise();
        Transaction finalisedTransaction = ongoingTransaction;
        ongoingTransaction.getAssociatedCustomer().getCart().setEmpty();
        ongoingTransaction = null; // Ready for a new transaction
        return finalisedTransaction;
    }
}
