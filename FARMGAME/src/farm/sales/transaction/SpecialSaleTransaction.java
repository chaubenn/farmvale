package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.Product;
import farm.sales.ReceiptPrinter;
import java.util.*;

/**
 * A transaction type that builds on the functionality of a categorised transaction,
 * allowing store-wide discounts to be applied to all products of a nominated type.
 */
public class SpecialSaleTransaction extends CategorisedTransaction {
    /**
     * Map allocating all discounts for each Product type.
     */
    private final Map<Barcode, Integer> discounts;

    /**
     * Construct a new special sale transaction for an associated customer, with an empty
     * set of discounts (i.e. no products are to be sold at a discount).
     */
    public SpecialSaleTransaction(Customer customer) {
        super(customer);
        this.discounts = new HashMap<>();
    }

    /**
     * Construct a new special sale transaction for an associated customer,
     * with a set of discounts to be applied to nominated product types on purchasing.
     */
    public SpecialSaleTransaction(Customer customer, Map<Barcode, Integer> discounts) {
        super(customer);
        this.discounts = new HashMap<>(discounts);
    }

    /**
     * Retrieves the discount percentage that will be applied for a particular product type,
     * as an integer (e.g. for a 10% discount, this method should return 10).
     */
    public int getDiscountAmount(Barcode type) {
        return discounts.getOrDefault(type, 0);
    }

    /**
     * Determines the total price for the provided product type within this transaction,
     * with any specified discount applied as an integer percentage taken from the usual subtotal.
     */
    @Override
    public int getPurchaseSubtotal(Barcode type) {
        int subtotal = super.getPurchaseSubtotal(type); // Get the base subtotal without discount
        int discount = getDiscountAmount(type); // Get the applicable discount for this product type
        return subtotal - (subtotal * discount / 100); // Apply the discount and return the adjusted
    }

    /**
     * Calculates the total price (with discounts) of all the current products in the transaction.
     */
    @Override
    public int getTotal() {
        int total = 0;
        // Iterate over all purchased types and accumulate the total with discounts applied
        for (Barcode type : getPurchasedTypes()) {
            total += getPurchaseSubtotal(type);
        }
        return total;
    }

    /**
     * Calculates how much the customer has saved from discounts.
     */
    public int getTotalSaved() {
        int totalSaved = 0;
        // Iterate over all purchased types and calculate the total savings from discounts
        for (Barcode type : getPurchasedTypes()) {
            int subtotal = super.getPurchaseSubtotal(type); // Original subtotal before discount
            int discount = getDiscountAmount(type); // Get the discount percentage for this type
            totalSaved += subtotal * discount / 100; // Calculate and accumulate the savings
        }
        return totalSaved;
    }

    /**
     * Converts the transaction into a formatted receipt for display, using the ReceiptPrinter.
     */
    @Override
    public String getReceipt() {
        // If the transaction is not finalized, return an active receipt placeholder
        if (!isFinalised()) {
            return ReceiptPrinter.createActiveReceipt();
        }

        List<List<String>> entries = new ArrayList<>();

        // Iterate over all barcode types in the enum to build receipt entries
        for (Barcode type : Barcode.values()) {
            // Check if the current barcode type was purchased
            if (getPurchasedTypes().contains(type)) {
                int quantity = getPurchaseQuantity(type);
                int price = getPurchasesByType().get(type).getFirst().getBasePrice();
                int subtotal = getPurchaseSubtotal(type);

                // Convert price and subtotal from cents to dollars as strings
                String priceString = "$" + (price / 100) + "."
                        + (price % 100 < 10 ? "0" : "")
                        + (price % 100);
                String subtotalString = "$" + (subtotal / 100) + "."
                        + (subtotal % 100 < 10 ? "0" : "")
                        + (subtotal % 100);

                // Build the entry for this product type
                List<String> entry = new ArrayList<>();
                entry.add(type.getDisplayName());             // Product name
                entry.add(String.valueOf(quantity));          // Quantity
                entry.add(priceString);                       // Price per unit
                entry.add(subtotalString);                    // Subtotal after discount

                // Add discount information if a discount was applied
                if (getDiscountAmount(type) > 0) {
                    String discountInfo = "Discount applied! "
                            + getDiscountAmount(type) + "% off " + type.getDisplayName();
                    entry.add(discountInfo);
                }
                entries.add(entry); // Add the entry to the list of entries
            }
        }

        // Calculate the total and total savings for the receipt
        int totalCents = getTotal();
        String total = "$" + (totalCents / 100) + "."
                + (totalCents % 100 < 10 ? "0" : "") + (totalCents % 100);

        int totalSavedCents = getTotalSaved();
        String totalSaved = "$" + (totalSavedCents / 100) + "."
                + (totalSavedCents % 100 < 10 ? "0" : "") + (totalSavedCents % 100);

        // If savings were made, include them in the receipt
        if (getTotalSaved() > 0) {
            return ReceiptPrinter.createReceipt(
                    List.of("Item", "Qty", "Price (ea.)", "Subtotal"),
                    entries, total, getAssociatedCustomer().getName(), totalSaved);
        } else {
            // Otherwise, just include the total
            return ReceiptPrinter.createReceipt(
                    List.of("Item", "Qty", "Price (ea.)", "Subtotal"),
                    entries, total, getAssociatedCustomer().getName());
        }
    }


    /**
     * Returns a string representation of this transaction and its current state.
     */
    @Override
    public String toString() {
        String status = isFinalised() ? "Finalised" : "Active"; // Determine the status
        StringBuilder productsString = new StringBuilder();

        // Build a string listing all products in the transaction
        for (Product product : getPurchases()) {
            if (!productsString.isEmpty()) {
                productsString.append(", ");
            }
            productsString.append(product.toString());
        }

        // Convert the discounts map to a string
        String discountsString = discounts.toString();

        // Build the string representation of the transaction

        return "Transaction {Customer: "
                + getAssociatedCustomer().getName() + " | Phone Number: "
                + getAssociatedCustomer().getPhoneNumber() + " | Address: "
                + getAssociatedCustomer().getAddress() + ", Status: "
                + status + ", Associated Products: [" + productsString
                + "], Discounts: " + discountsString + "}";
    }
}