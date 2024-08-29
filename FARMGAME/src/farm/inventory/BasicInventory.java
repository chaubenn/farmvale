package farm.inventory;

import farm.inventory.product.data.*;
import farm.inventory.product.*;
import farm.core.*;
import java.util.*;

/**
 * A very basic inventory that both stores and handles products individually.
 * Only supports operation on single Products at a time.
 */
public class BasicInventory implements Inventory {

    /**
     * List of Products in Inventory.
     **/
    private final List<Product> products;

    /**
     * Creates new instance of BasicInventory with an empty ArrayList.
     */
    public BasicInventory() {
        this.products = new ArrayList<>();
    }

    /**
     * Adds a new product with corresponding barcode to the inventory.
     */
    @Override
    public void addProduct(Barcode barcode, Quality quality) {
        products.add(createProduct(barcode, quality));
    }

    /**
     * Adds a new product with corresponding barcode and quantity to the inventory.
     */
    @Override
    public void addProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException {
        // Quantity can be 1
        if (quantity == 1) {
            products.add(createProduct(barcode, quality));
        } else {
            // Else or if Quantity != 1, exception
            throw new InvalidStockRequestException("Current inventory is "
                    + "not fancy enough. Please supply products one at a time.");
        }
    }

    /**
     * Determines if a product exists in the inventory with the given barcode.
     */
    @Override
    public boolean existsProduct(Barcode barcode) {
        // Product exists if there is a product in products that has barcode.
        for (Product product : products) {
            if (product.getBarcode().equals(barcode)) {
                return true;
            }
        }
        // If loop iterates through products without returning, product does not exist.
        return false;
    }

    /**
     * Removes the first product with corresponding barcode from the inventory.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode) {
        // Iterates through the product list to find and remove the first target product.
        for (Product product : products) {
            if (product.getBarcode().equals(barcode)) {
                products.remove(product);
                return List.of(product);
            }
        }
        // Return empty list if product doesn't exist in products.
        return List.of();
    }

    /**
     * Adds a new product with corresponding barcode and quantity to the inventory.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode, int quantity)
            throws FailedTransactionException {
        // Quantity can be 1, if so return list of Product
        if (quantity == 1) {
            List<Product> list = removeProduct(barcode);
            if (!list.isEmpty()) {
                return list;
            }
        } else {
            throw new FailedTransactionException("Current inventory is not fancy enough. "
                    + "Please purchase products one at a time.");
        }
        // Return empty list if Product doesn't exist or quantity != 1
        return List.of();
    }

    /**
     * Retrieves the full stock currently held in the inventory.
     */
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Creates a product instance based on barcode and quality.
     */
    protected Product createProduct(Barcode barcode, Quality quality) {
        return switch (barcode) {
            case EGG -> new Egg(quality);
            case MILK -> new Milk(quality);
            case JAM -> new Jam(quality);
            case WOOL -> new Wool(quality);
        };
    }
}
