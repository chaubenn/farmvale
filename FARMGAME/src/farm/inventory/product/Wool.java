package farm.inventory.product;

import farm.inventory.product.data.*;

/**
 * A class representing an instance of an egg.
 */
public class Wool extends Product {
    /**
     * The predefined barcode for eggs.
     **/
    private static final Barcode WOOL_BARCODE = Barcode.WOOL;

    /**
     * Create a Wool instance with no additional details.
     */
    public Wool() {
        super(WOOL_BARCODE, Quality.REGULAR);
    }

    /**
     * Create a Wool instance with a quality value.
     */
    public Wool(Quality quality) {
        super(WOOL_BARCODE, quality);
    }
}
