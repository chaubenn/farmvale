package farm.inventory;

import farm.inventory.product.Product;
import farm.inventory.product.data.*;
import farm.core.*;
import farm.inventory.product.*;

import java.util.*;

/**
 * A fancy inventory which stores products in stacks, enabling quantity information.
 * Introduces the concept of performing operations on multiple Products, such as removing 4 Eggs.
 */
public class FancyInventory implements Inventory {

    /**
     * List of stacks, each stack representing a product type in the inventory.
     **/
    private final List<Stack<Product>> inventory;

    /**
     * Creates new instance of FancyInventory with empty stacks for each product type.
     */
    public FancyInventory() {
        this.inventory = new ArrayList<>();
        // Initialize a stack for each product type based on its barcode.
        for (Barcode ignored : Barcode.values()) {
            inventory.add(new Stack<>());
        }
    }

    /**
     * Adds a new product with corresponding barcode to the inventory.
     */
    @Override
    public void addProduct(Barcode barcode, Quality quality) {
        // Push the created product onto the stack corresponding to its barcode.
        // Ordinal used to find index position of barcode.
        inventory.get(barcode.ordinal()).push(createProduct(barcode, quality));
    }

    /**
     * Adds multiple of the product with corresponding barcode to the inventory.
     */
    @Override
    public void addProduct(Barcode barcode, Quality quality, int quantity)
            throws InvalidStockRequestException {
        // Add the specified quantity of products to the inventory.
        for (int i = 0; i < quantity; i++) {
            addProduct(barcode, quality);
        }
    }

    /**
     * Determines if a product exists in the inventory with the given barcode.
     */
    @Override
    public boolean existsProduct(Barcode barcode) {
        // Check if the stack corresponding to the barcode is not empty.
        return !inventory.get(barcode.ordinal()).isEmpty();
    }

    /**
     * Removes the highest quality product with corresponding barcode from the inventory.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode) {
        // Check if product exists before attempting to remove it.
        if (!existsProduct(barcode)) {
            return new ArrayList<>();
        }
        // Get the stack corresponding to the barcode
        Stack<Product> stack = inventory.get(barcode.ordinal());
        if (stack == null || stack.isEmpty()) {
            return new ArrayList<>();
        }
        // Find and remove the product with the highest quality
        Product highestQualityProduct = null;
        int highestQualityIndex = -1;

        for (int i = 0; i < stack.size(); i++) {
            Product currentProduct = stack.get(i);
            if (highestQualityProduct == null || currentProduct.getQuality().ordinal()
                    > highestQualityProduct.getQuality().ordinal()) {
                highestQualityProduct = currentProduct;
                highestQualityIndex = i;
            }
        }
        stack.remove(highestQualityIndex); // Remove the highest quality product from the stack
        List<Product> removedProducts = new ArrayList<>();
        if (highestQualityProduct != null) {
            removedProducts.add(highestQualityProduct);
        }

        return removedProducts;
    }

    /**
     * Removes a given number of products with corresponding barcode from the inventory,
     * choosing the highest quality products possible.
     */
    @Override
    public List<Product> removeProduct(Barcode barcode, int quantity)
            throws FailedTransactionException {
        // Get the stack corresponding to the barcode
        Stack<Product> stack = inventory.get(barcode.ordinal());

        if (stack == null || stack.isEmpty()) {
            return new ArrayList<>();
        }
        List<Product> removedProducts = new ArrayList<>();
        for (int j = 0; j < quantity; j++) {
            Product highestQualityProduct = null;
            int highestQualityIndex = -1;
            // Find the highest quality product in the stack
            for (int i = 0; i < stack.size(); i++) {
                Product currentProduct = stack.get(i);
                if (highestQualityProduct == null || currentProduct.getQuality().ordinal()
                        > highestQualityProduct.getQuality().ordinal()) {
                    highestQualityProduct = currentProduct;
                    highestQualityIndex = i;
                }
            }
            if (highestQualityIndex != -1) {
                stack.remove(highestQualityIndex);
                // Remove the highest quality product from the stack
                removedProducts.add(highestQualityProduct);
            }
        }
        return removedProducts;
    }

    /**
     * Retrieves the full stock currently held in the inventory.
     */
    @Override
    public List<Product> getAllProducts() {
        List<Product> allProducts = new ArrayList<>();
        for (Barcode barcode : Barcode.values()) {
            // Get the list of products associated with the current barcode
            List<Product> products = inventory.get(barcode.ordinal());
            // If there are products associated with this barcode, add them to the allProducts list
            if (products != null) {
                allProducts.addAll(products);
            }
        }
        return allProducts;
    }


    /**
     * Get the quantity of a specific product in the inventory.
     */
    public int getStockedQuantity(Barcode barcode) {
        // Return the size of the stack corresponding to the barcode.
        return inventory.get(barcode.ordinal()).size();
    }

    /**
     * * Creates a product instance based on barcode and quality.
     */
    protected Product createProduct(Barcode barcode, Quality quality) {
        return switch (barcode) {
            case EGG -> new Egg(quality);
            case MILK -> new Milk(quality);
            case WOOL -> new Wool(quality);
            case JAM -> new Jam(quality);
        };
    }
}
