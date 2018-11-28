package fall2018.csc2017.game_centre.twenty_forty;

import java.io.Serializable;
import java.util.Objects;
import fall2018.csc2017.game_centre.R;

public class Box implements Serializable {
    private int exponent;
    private int background;
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

    Box(int exponent) {
        this.exponent = exponent;
        this.background = BOX_BACKGROUNDS[exponent];
    }

    public boolean isEmptyBox() {
        return exponent == 0;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int newExponent) {
        this.exponent = newExponent;
        this.background = BOX_BACKGROUNDS[newExponent];
    }

    public int getBackground() {
        return background;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        else if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return exponent == box.exponent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(exponent);
    }
}
