package fall2018.csc2017.game_centre.twenty_forty;

import java.util.Objects;

public class Box {
    private int exponent;
    //TODO: Need to make power of 2s tiles: 2,4,8,16,32,64,.... (Photoshop time)

    Box(int exponent) {
        this.exponent = exponent;
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
