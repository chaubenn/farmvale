package farm.inventory.product;

import farm.inventory.product.data.*;

/**
 * A class representing an instance of a Jam.
 */
public class Jam extends Product {

    /**
     * The predefined barcode for Jam.
     **/
    private static final Barcode JAM_BARCODE = Barcode.JAM;

    /**
     * Create a Jam instance with no additional details.
     */
    public Jam() {
        super(JAM_BARCODE, Quality.REGULAR);
    }

    /**
     * Create a Jam instance with a quality value.
     */
    public Jam(Quality quality) {
        super(JAM_BARCODE, quality);
    }
}
