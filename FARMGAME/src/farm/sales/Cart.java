package farm.sales;

import farm.inventory.product.Product;
import java.util.*;

/**
 * A shopping cart that stores the customer products until they check out.
 */
public class Cart {
    /**
     * List of Products in cart.
     */
    private final List<Product> cart;

    /**
     * Constructs an empty Cart.
     */
    public Cart() {
        cart = new ArrayList<>();
    }

    /**
     * Adds a given product to the shopping cart.
     */
    public void addProduct(Product product) {
        cart.add(product);
    }

    /**
     * Retrieves all the products in the Cart in the order they were added.
     */
    public List<Product> getContents() {
        return new ArrayList<>(cart);
    }

    /**
     * Empty out the shopping cart.
     */
    public void setEmpty() {
        cart.clear();
    }

    /**
     * Returns if the cart is empty
     */
    public boolean isEmpty() {
        return cart.isEmpty();
    }
}
