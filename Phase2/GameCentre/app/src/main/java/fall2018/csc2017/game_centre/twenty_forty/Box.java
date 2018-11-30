package fall2018.csc2017.game_centre.twenty_forty;

import java.io.Serializable;
import java.util.Objects;

import fall2018.csc2017.game_centre.R;

public class Box implements Serializable {
    /**
     * If exponent > 0, tile displays 2^exponent. Blank tile otherwise
     */
    private int exponent;

    /**
     * Background index of the tile
     */
    private int background;

    /**
     * Array containing references to the images a TwentyFourty box might have
     */
    private final static int[] BOX_BACKGROUNDS = {
            R.drawable.box_0,
            R.drawable.box_2,
            R.drawable.box_4,
            R.drawable.box_8,
            R.drawable.box_16,
            R.drawable.box_32,
            R.drawable.box_64,
            R.drawable.box_128,
            R.drawable.box_256,
            R.drawable.box_512,
            R.drawable.box_1024,
            R.drawable.box_2048,
    };

    /**
     * Constructs a Box with the given exponent
     *
     * @param exponent 0 represents the empty Box, otherwise the Box represents 2^exponent
     */
    Box(int exponent) {
        this.exponent = exponent;
        this.background = BOX_BACKGROUNDS[exponent];
    }

    /**
     * Tells if the Box is the empty Box
     *
     * @return True iff exponent == 0
     */
    boolean isEmptyBox() {
        return exponent == 0;
    }

    /**
     * Gets the exponent contained in this Box
     *
     * @return the exponent which was set to this Box at construction
     */
    int getExponent() {
        return exponent;
    }

    /**
     * Sets the exponent of this Box
     *
     * @param newExponent 0 represents the empty Box, otherwise sets to a new exponent as normal
     */
    void setExponent(int newExponent) {
        this.exponent = newExponent;
        this.background = BOX_BACKGROUNDS[newExponent];
    }

    /**
     * Gets the background image reference for this Box
     *
     * @return This Box's image from R.drawable
     */
    public int getBackground() {
        return background;
    }

    /**
     * Checks for equality between this Box and object o
     *
     * @param o A valid Object
     * @return True iff o is a Box and has the same exponent as this Box
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return exponent == box.exponent;
    }

    /**
     * Gets a hash of exponent
     *
     * @return The output of Objects.hash when given this Box's exponent as input
     */
    @Override
    public int hashCode() {
        return Objects.hash(exponent);
    }
}
