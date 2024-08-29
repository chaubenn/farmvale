package farm.inventory.product;

import farm.inventory.product.data.*;

/**
 * A class representing an instance of Milk.
 */
public class Milk extends Product {

    /**
     * The predefined barcode for Milk.
     **/
    private static final Barcode MILK_BARCODE = Barcode.MILK;

    /**
     * Create a Milk instance with no additional details.
     */
    public Milk() {
        super(MILK_BARCODE, Quality.REGULAR);
    }

    /**
     * Create a Milk instance with a quality value.
     */
    public Milk(Quality quality) {
        super(MILK_BARCODE, quality);
    }
}
