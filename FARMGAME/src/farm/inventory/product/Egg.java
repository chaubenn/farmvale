package farm.inventory.product;

import farm.inventory.product.data.*;

/**
 * A class representing an instance of an egg.
 */
public class Egg extends Product {

    /**
     * The predefined barcode for eggs.
     **/
    private static final Barcode EGG_BARCODE = Barcode.EGG;

    /**
     * Create an egg instance with no additional details.
     */
    public Egg() {
        super(EGG_BARCODE, Quality.REGULAR);
    }

    /**
     * Create an egg instance with a quality value.
     */
    public Egg(Quality quality) {
        super(EGG_BARCODE, quality);
    }
}
