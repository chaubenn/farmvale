package farm.core;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.inventory.product.data.*;
import farm.inventory.*;
import farm.sales.transaction.Transaction;
import farm.sales.*;
import farm.customer.AddressBook;

import java.util.List;

/**
 * Top-level model class responsible for storing and making updates to the data and
 * smaller model entities that make up the internal state of a farm.
 */
public class Farm {
    /**
     * The inventory through which access to the farm's stock is provisioned.
     */
    private final Inventory inventory;

    /**
     * The address book storing the farm's customer records.
     */
    private final AddressBook addressBook;

    /**
     * TransactionManager of the farm.
     */
    private final TransactionManager transactionManager;

    /**
     * The history of the farm's transactions.
     */
    private final TransactionHistory transactionHistory;

    /**
     * Creates a new farm instance with an inventory and address book supplied.
     */
    public Farm(Inventory inventory, AddressBook addressBook) {
        this.inventory = inventory;
        this.addressBook = addressBook;
        this.transactionManager = new TransactionManager();
        this.transactionHistory = new TransactionHistory();
    }

    /**
     * Saves the supplied customer in the farm's address book.
     */
    public void saveCustomer(Customer customer) throws DuplicateCustomerException {
        addressBook.addCustomer(customer);
    }

    /**
     * Sets the provided transaction as the current ongoing transaction.
     */
    public void startTransaction(Transaction transaction) throws FailedTransactionException {
        // Ensure that no other transaction is currently ongoing
        if (transactionManager.hasOngoingTransaction()) {
            throw new FailedTransactionException("A transaction is already ongoing.");
        }
        transactionManager.setOngoingTransaction(transaction);
    }

    /**
     * Attempts to add a single product of the given type to the customer's shopping cart.
     */
    public int addToCart(Barcode barcode) throws FailedTransactionException {
        // Ensure that a transaction is currently ongoing before proceeding
        checkTransactionOngoing();

        // Attempt to remove the product from inventory
        List<Product> products = inventory.removeProduct(barcode);
        if (products.isEmpty()) {
            return 0; // No product found, return 0 indicating failure to add to cart
        }
        // Register the product in the ongoing transaction
        transactionManager.registerPendingPurchase(products.getFirst());
        return 1; // Return 1 indicating success
    }

    /**
     * Attempts to add the specified number of products of the given type to the customer's shopping cart.
     */
    public int addToCart(Barcode barcode, int quantity) throws FailedTransactionException {
        // Ensure that a transaction is currently ongoing before proceeding
        checkTransactionOngoing();

        // Attempt to remove the specified quantity of the product from inventory
        List<Product> products = inventory.removeProduct(barcode, quantity);
        if (products.isEmpty()) {
            return 0; // No products found, return 0 indicating failure to add to cart
        }

        // Register each product in the ongoing transaction
        for (Product product : products) {
            transactionManager.registerPendingPurchase(product);
        }
        return products.size(); // Return the number of products successfully added to the cart
    }

    /**
     * Closes the ongoing transaction.
     */
    public boolean checkout() throws FailedTransactionException {
        // Ensure that a transaction is currently ongoing before proceeding
        checkTransactionOngoing();

        // Finalize the transaction and record it in the transaction history
        Transaction transaction = transactionManager.closeCurrentTransaction();
        if (transaction.getPurchases().isEmpty()) {
            return false; // No products purchased, return false indicating unsuccessful checkout
        }

        transactionHistory.recordTransaction(transaction);
        return true; // Return true indicating successful checkout
    }

    /**
     * Retrieves all customer records currently stored in the farm's address book.
     */
    public List<Customer> getAllCustomers() {
        return addressBook.getAllRecords();
    }

    /**
     * Retrieves all products currently stored in the farm's inventory.
     */
    public List<Product> getAllStock() {
        return inventory.getAllProducts();
    }

    /**
     * Retrieves a customer from the address book.
     */
    public Customer getCustomer(String name, int phoneNumber) throws CustomerNotFoundException {
        return addressBook.getCustomer(name, phoneNumber);
    }

    /**
     * Retrieves the receipt associated with the most recent transaction.
     */
    public String getLastReceipt() {
        Transaction lastTransaction = transactionHistory.getLastTransaction();
        // Return the receipt if a last transaction exists, otherwise return null
        return (lastTransaction != null) ? lastTransaction.getReceipt() : null;
    }

    /**
     * Retrieves the farm's transaction history.
     */
    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    /**
     * Retrieves the farm's transaction manager.
     */
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    /**
     * Adds a single product of the specified type and quality to the farm's inventory.
     */
    public void stockProduct(Barcode barcode, Quality quality) {
        inventory.addProduct(barcode, quality);
    }

    /**
     * Adds some quantity of products of the given type and quality to the farm's inventory.
     */
    public void stockProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        // Handle stocking differently based on the type of inventory
        if (!(inventory instanceof FancyInventory)) {
            if (quantity > 1) {
                throw new InvalidStockRequestException("Only FancyInventory supports adding more "
                        + "than one product at a time.");
            }
            inventory.addProduct(barcode, quality);
            // Stock a single product if quantity is 1
        } else {
            inventory.addProduct(barcode, quality, quantity);
            // Stock multiple products if supported
        }
    }

    /**
     * Checks if a transaction is ongoing and throws an exception if none is found.
     */
    private void checkTransactionOngoing() throws FailedTransactionException {
        // Throw an exception if no transaction is currently ongoing
        if (!transactionManager.hasOngoingTransaction()) {
            throw new FailedTransactionException("Cannot add to cart when no "
                    + "customer has started shopping.");
        }
    }
}
