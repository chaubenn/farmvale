package farm.sales;

import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.sales.transaction.*;

import java.util.*;

/**
 * A record of all past transactions. Handles retrieval of statistics about
 * past transactions, such as earnings and popular products.
 */
public class TransactionHistory {
    /**
     * List of transaction history.
     */
    private final List<Transaction> transactions;

    /**
     * Constructs an empty TransactionHistory.
     */
    public TransactionHistory() {
        transactions = new ArrayList<>();
    }

    /**
     * Adds the given transaction to the record of all past transactions.
     */
    public void recordTransaction(Transaction transaction) {
        // Only add the transaction if it has been finalized
        if (transaction.isFinalised()) {
            transactions.add(transaction);
        }
    }

    /**
     * Retrieves the most recent transaction.
     */
    public Transaction getLastTransaction() {
        // Return the last transaction in the list if it exists, otherwise return null
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.getLast();
    }

    /**
     * Calculates the gross earnings, i.e. total income, from all transactions.
     */
    public int getGrossEarnings() {
        int totalEarnings = 0;
        // Sum up the total from each transaction to calculate the gross earnings
        for (Transaction transaction : transactions) {
            totalEarnings += transaction.getTotal();
        }
        return totalEarnings;
    }

    /**
     * Calculates the gross earnings, i.e. total income, from all sales of a particular product type.
     */
    public int getGrossEarnings(Barcode type) {
        int totalEarnings = 0;
        for (Transaction transaction : transactions) {
            // Iterate over all products in each transaction
            for (Product product : transaction.getPurchases()) {
                // Check if the product's barcode matches the specified type
                if (product.getBarcode().equals(type)) {
                    // Add the product's base price to the total earnings
                    totalEarnings += product.getBasePrice();
                }
            }
        }
        return totalEarnings;
    }


    /**
     * Calculates the number of transactions made.
     */
    public int getTotalTransactionsMade() {
        // Return the number of transactions recorded
        return transactions.size();
    }

    /**
     * Calculates the number of products sold over all transactions.
     */
    public int getTotalProductsSold() {
        int totalProductsSold = 0;
        // Sum the number of products sold in each transaction to get the total products sold
        for (Transaction transaction : transactions) {
            totalProductsSold += transaction.getPurchases().size();
        }
        return totalProductsSold;
    }

    /**
     * Calculates the number of sold of a particular product type, over all transactions.
     */
    public int getTotalProductsSold(Barcode type) {
        int totalProductsSold = 0;
        for (Transaction transaction : transactions) {
            // Iterate over all products in each transaction
            for (Product product : transaction.getPurchases()) {
                // Check if the product's barcode matches the specified type
                if (product.getBarcode().equals(type)) {
                    // Increment the total count of products sold
                    totalProductsSold++;
                }
            }
        }
        return totalProductsSold;
    }


    /**
     * Retrieves the transaction with the highest gross earnings,
     * i.e. reported total. If there are multiple return the one that first was recorded.
     */
    public Transaction getHighestGrossingTransaction() {
        // If no transactions have been recorded, return null
        if (transactions.isEmpty()) {
            return null;
        }

        // Initialize the highest grossing transaction as the first transaction
        Transaction highestGrossing = transactions.getFirst();

        // Iterate through all transactions to find the one with the highest total
        for (Transaction transaction : transactions) {
            if (transaction.getTotal() > highestGrossing.getTotal()) {
                highestGrossing = transaction;
            }
        }
        return highestGrossing;
    }

    /**
     * Calculates which type of product has had the highest quantity sold overall.
     */
    public Barcode getMostPopularProduct() {
        // If no transactions have been recorded, return a default product (EGG)
        if (transactions.isEmpty()) {
            return Barcode.EGG;
        }

        int maxCount = 0;
        Barcode mostPopular = null;

        // Iterate through all Barcode values to determine which has the highest sales count
        for (Barcode barcode : Barcode.values()) {
            int count = getTotalProductsSold(barcode);
            if (count > maxCount) {
                maxCount = count;
                mostPopular = barcode;
            }
        }
        return mostPopular;
    }

    /**
     * Calculates the average amount spent by customers across all transactions.
     */
    public double getAverageSpendPerVisit() {
        // If no transactions have been recorded, return 0.0
        if (transactions.isEmpty()) {
            return 0.0;
        }
        // Calculate the average by dividing gross earnings by the number of transactions
        return (double) getGrossEarnings() / getTotalTransactionsMade();
    }

    /**
     * Calculates the average amount a product has been discounted by,
     * across all sales of that product.
     */
    public double getAverageProductDiscount(Barcode type) {
        double totalDiscountAmount = 0;
        int totalDiscountedProducts = 0;

        // Iterate over all transactions to calculate total discounts and count discounted products
        for (Transaction transaction : transactions) {
            if (transaction instanceof SpecialSaleTransaction specialTransaction) {
                int discount = specialTransaction.getDiscountAmount(type);

                if (discount > 0) {
                    int quantity = specialTransaction.getPurchaseQuantity(type);
                    totalDiscountAmount += (discount * quantity);
                    totalDiscountedProducts += quantity;
                }
            }
        }

        // If no products were discounted, return 0.0
        if (totalDiscountedProducts == 0) {
            return 0.0;
        }

        // Calculate and return the average discount by dividing total discounts
        // by the number of discounted products
        return totalDiscountAmount / totalDiscountedProducts;
    }
}
