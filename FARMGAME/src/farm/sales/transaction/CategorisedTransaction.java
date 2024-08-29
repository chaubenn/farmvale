package farm.sales.transaction;

import farm.customer.Customer;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.Product;
import farm.sales.ReceiptPrinter;

import java.util.*;

/**
 * A transaction type that allows products to be categorised by
 * their types, not solely as isolated individual products. The resulting
 * receipt therefore displays purchased types with an associated quantity
 * purchased and subtotal, rather than a single line for each product.
 */
public class CategorisedTransaction extends Transaction {

    /**
     * Constructs a CategorisedTransaction for a given customer.
     */
    public CategorisedTransaction(Customer customer) {
        super(customer);
    }

    /**
     * Retrieves all unique product types of the purchases associated with the transaction.
     */
    public Set<Barcode> getPurchasedTypes() {
        Set<Barcode> types = new HashSet<>();
        for (Product product : getPurchases()) {
            types.add(product.getBarcode());
        }
        return types;
    }

    /**
     * Retrieves all products associated with the transaction, grouped by their type.
     */
    public Map<Barcode, List<Product>> getPurchasesByType() {
        Map<Barcode, List<Product>> purchasesByType = new HashMap<>();
        for (Product product : getPurchases()) {
            Barcode barcode = product.getBarcode();
            // Iff productList if null, create new array and put barcode, productList
            List<Product> productList =
                    purchasesByType.computeIfAbsent(barcode, k -> new ArrayList<>());
            productList.add(product);
        }
        return purchasesByType;
    }


    /**
     * Retrieves the number of products of a particular type associated with the transaction.
     */
    public int getPurchaseQuantity(Barcode type) {
        if (getPurchasesByType().containsKey(type)) {
            return getPurchasesByType().get(type).size();
        }
        // If the type is not found in the map, return 0
        return 0;
    }


    /**
     * Determines the total price for the provided product type within this transaction.
     */
    public int getPurchaseSubtotal(Barcode type) {
        int subtotal = 0;
        // If the type exists in the map, return the base price multiplied by number of Products,
        // since getPurchasesByType automatically groups by product type, hence BasePrices are equal
        if (getPurchasesByType().containsKey(type)) {
            subtotal += type.getBasePrice() * getPurchasesByType().get(type).size();
        }
        return subtotal;
    }


    /**
     * Converts the transaction into a formatted receipt for display, using the ReceiptPrinter.
     */
    @Override
    public String getReceipt() {
        if (!isFinalised()) {
            // Return a placeholder receipt if the transaction isn't finalized
            return ReceiptPrinter.createActiveReceipt();
        }
        List<List<String>> entries = new ArrayList<>();

        for (Barcode type : Barcode.values()) {
            if (getPurchasedTypes().contains(type)) {
                int quantity = getPurchaseQuantity(type);
                int price = getPurchasesByType().get(type).getFirst().getBasePrice();
                int subtotal = getPurchaseSubtotal(type);

                // Convert price and subtotal from cents to dollars as strings
                String priceString = "$" + (price / 100) + "." + (price % 100 < 10 ? "0" : "")
                        + (price % 100);
                String subtotalString = "$" + (subtotal / 100) + "."
                        + (subtotal % 100 < 10 ? "0" : "") + (subtotal % 100);

                entries.add(List.of(
                        type.getDisplayName(),            // Item name
                        String.valueOf(quantity),         // Quantity
                        priceString,                      // Price (ea.)
                        subtotalString                    // Subtotal
                ));
            }
        }
        // Calculate the total cost and convert to dollars as a string
        int totalCents = getTotal();
        String totalString = "$" + (totalCents / 100) + "."
                + (totalCents % 100 < 10 ? "0" : "") + (totalCents % 100);

        // Create and return the formatted receipt
        return ReceiptPrinter.createReceipt(
                List.of("Item", "Qty", "Price (ea.)", "Subtotal"),  // Header
                entries,                                            // Entries
                totalString,                                        // Total
                getAssociatedCustomer().getName()                   // Customer name
        );
    }
}


