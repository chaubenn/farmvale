package farm.inventory;

import farm.inventory.product.Product;
import farm.inventory.product.data.Barcode;
import farm.inventory.product.data.Quality;
import farm.core.InvalidStockRequestException;
import farm.core.FailedTransactionException;

import java.util.List;

/**
 * An interface representing the base requirements for an Inventory.
 * Handles adding and removing items from storage.
 */
public interface Inventory {

    /**
     * Adds a new product with the corresponding barcode to the inventory.
     */
    void addProduct(Barcode barcode, Quality quality);

    /**
     * Adds the specified quantity of the product with the corresponding barcode to the inventory,
     * provided that the implementing inventory supports adding multiple products at once.
     */
    void addProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException;

    /**
     * Determines if a product exists in the inventory with the given barcode.
     */
    boolean existsProduct(Barcode barcode);

    /**
     * Removes the first product with the corresponding barcode from the inventory.
     */
    List<Product> removeProduct(Barcode barcode);

    /**
     * Removes the given number of products with the corresponding barcode from the inventory,
     * provided that the implementing inventory supports removing multiple products at once.
     */
    List<Product> removeProduct(Barcode barcode, int quantity) throws FailedTransactionException;

    /**
     * Retrieves the full stock currently held in the inventory.
     */
    List<Product> getAllProducts();
}
