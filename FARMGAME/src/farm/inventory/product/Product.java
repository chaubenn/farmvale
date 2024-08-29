package farm.inventory.product;

import java.util.Objects;
import farm.inventory.product.data.*;

/**
 * An abstract class representing an instance of a product.
 * Each product is a single instance of a specific item.
 */
public abstract class Product {

    /**
     * Barcode of Product.
     */
    private final Barcode barcode;

    /**
     * Quality of Product.
     */
    private final Quality quality;

    /**
     * Create a Product instance with Barcode and Quality.
     */
    protected Product(Barcode barcode, Quality quality) {
        this.barcode = barcode;
        this.quality = quality;
    }

    /**
     * Accessor method for the product's identifier.
     */
    public Barcode getBarcode() {
        return barcode;
    }

    /**
     * Retrieve the products base sale price.
     */
    public int getBasePrice() {
        return barcode.getBasePrice();
    }

    /**
     * Retrieve the product's display name, for visual/textual representation.
     */
    public String getDisplayName() {
        return barcode.getDisplayName();
    }

    /**
     * Retrieve the product's quality.
     */
    public Quality getQuality() {
        return quality;
    }

    /**
     * Returns a string representation of this product class.
     */
    @Override
    public String toString() {
        return getDisplayName() + ": " + getBasePrice() + "c *" + quality + "*";
    }

    /**
     * If two instances of product are equal to each other.
     */
    @Override
    public boolean equals(Object obj) {
        // If the object is not a Product, return false. Also assign pattern variable.
        if (!(obj instanceof Product product2)) {
            return false;
        }
        // Customers are equal if phone numbers and names match.
        return Objects.equals(barcode, product2.barcode)
                && Objects.equals(quality, product2.quality);
    }

    /**
     * String name of Customer
     */
    @Override
    public int hashCode() {
        return Objects.hash(barcode, quality);
    }
}

