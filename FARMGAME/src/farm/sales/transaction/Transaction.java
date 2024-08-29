package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.Product;
import farm.sales.ReceiptPrinter;
import java.util.*;

/**
 * Transactions keep track of what items are to be (or have been) purchased and by whom.
 */
public class Transaction {
    /**
     * Customer associated with the transaction.
     */
    private final Customer customer;

    /**
     * List of products that the customer is purchasing.
     */
    private final List<Product> purchases;

    /**
     * Boolean flag to indicate whether the transaction is finalized.
     */
    private boolean isFinalised;

    /**
     * Constructs a new transaction for an associated customer.
     *
     * @param customer The customer associated with the transaction.
     */
    public Transaction(Customer customer) {
        this.customer = customer;
        // Initialize the purchases list with the contents of the customer's cart.
        this.purchases = new ArrayList<>(customer.getCart().getContents());
        this.isFinalised = false; // Initially, the transaction is not finalized.
    }

    /**
     * Retrieves the customer associated with this transaction.
     *
     * @return The associated customer.
     */
    public Customer getAssociatedCustomer() {
        return customer;
    }

    /**
     * Retrieves all products associated with the transaction.
     *
     * @return A list of products in the transaction.
     */
    public List<Product> getPurchases() {
        // If the transaction is finalized, return the finalized purchases.
        // Otherwise, return the current contents of the customer's cart.
        if (isFinalised) {
            return new ArrayList<>(purchases);
        } else {
            return new ArrayList<>(customer.getCart().getContents());
        }
    }

    /**
     * Calculates the total price of all the current products in the transaction.
     *
     * @return The total price in cents.
     */
    public int getTotal() {
        int total = 0;
        // Choose the correct product list based on whether the transaction is finalized.
        List<Product> products = isFinalised ? purchases : customer.getCart().getContents();
        // Sum the base prices of all products in the list.
        for (Product product : products) {
            total += product.getBasePrice();
        }
        return total;
    }

    /**
     * Determines if the transaction is finalized (i.e., sale completed) or not.
     *
     * @return True if the transaction is finalized, false otherwise.
     */
    public boolean isFinalised() {
        return isFinalised;
    }

    /**
     * Finalizes the transaction by storing the current contents of the customer's cart
     * as the finalized purchases and clearing the cart.
     */
    public void finalise() {
        if (!isFinalised) {
            // Clear the existing purchases list and add the current cart contents to it.
            purchases.clear();
            purchases.addAll(customer.getCart().getContents());
            // Clear the customer's cart as the transaction is now finalized.
            customer.getCart().setEmpty();
            isFinalised = true;
        }
    }

    /**
     * Returns a string representation of this transaction and its current state.
     *
     * @return A string representation of the transaction.
     */
    @Override
    public String toString() {
        // Determine the status of the transaction (finalized or active).
        String status = isFinalised ? "Finalised" : "Active";
        StringBuilder productsString = new StringBuilder();

        // Build a string listing all products in the transaction.
        for (Product product : getPurchases()) {
            if (!productsString.isEmpty()) {
                productsString.append(", ");
            }
            productsString.append(product.toString());
        }

        // Build the final string representation of the transaction.

        return "Transaction {Customer: " + customer.getName()
                + " | Phone Number: " + customer.getPhoneNumber() + " | Address: "
                + customer.getAddress() + ", Status: " + status + ", Associated Products: ["
                + productsString + "]}";
    }

    /**
     * Converts the transaction into a formatted receipt for display, using the ReceiptPrinter.
     *
     * @return The formatted receipt as a string.
     */
    public String getReceipt() {
        // If the transaction is not finalized, return a placeholder receipt.
        if (!isFinalised) {
            return ReceiptPrinter.createActiveReceipt();
        }

        List<List<String>> entries = new ArrayList<>();
        // Create a list of entries for each product in the transaction.
        for (Product product : purchases) {
            List<String> entry = new ArrayList<>();
            entry.add(product.getDisplayName());
            // Convert the price from cents to a string in dollars.
            int priceInCents = product.getBasePrice();
            String priceString = "$" + (priceInCents / 100) + "."
                    + (priceInCents % 100 < 10 ? "0" : "") + (priceInCents % 100);
            entry.add(priceString);
            entries.add(entry);
        }

        // Convert the total price from cents to a string in dollars.
        int totalInCents = getTotal();
        String totalString = "$" + (totalInCents / 100) + "."
                + (totalInCents % 100 < 10 ? "0" : "") + (totalInCents % 100);

        // Create and return the formatted receipt using the ReceiptPrinter.
        return ReceiptPrinter.createReceipt(
                List.of("Item", "Price"),
                entries,
                totalString,
                customer.getName()
        );
    }
}
