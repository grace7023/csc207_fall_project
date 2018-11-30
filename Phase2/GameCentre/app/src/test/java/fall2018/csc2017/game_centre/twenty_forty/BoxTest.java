package fall2018.csc2017.game_centre.twenty_forty;

import org.junit.Test;

import fall2018.csc2017.game_centre.R;

import static org.junit.Assert.*;

public class BoxTest {

    /**
     * Test whether isEmptyBox works
     */
    @Test
    public void isEmptyBox() {
        Box box = new Box(0);
        assertTrue(box.isEmptyBox());
    }

    /**
     * Test whether getBackground works
     */
    @Test
    public void getBackground() {
        Box box = new Box(1);
        assertEquals(box.getBackground(), R.drawable.box_2);
    }

    /**
     * Test whether equals works
     */
    @Test
    public void equals() {
        Box box1 = new Box(1);
        Box box2 = new Box(1);
        assertEquals(box1, box2);
    }

    /**
     * Test whether hashCode works
     */
    @Test
    public void hash(){
        Box box1 = new Box(1);
        Box box2 = new Box(1);
        assertEquals(box1.hashCode(), box2.hashCode());
    }

    /**
     * Test whether setExponent works
     */
    @Test
    public void setExponent(){
        Box box = new Box(1);
        box.setExponent(2);
        assertEquals(2,box.getExponent());
    }
}