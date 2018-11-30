package fall2018.csc2017.game_centre;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddonsTest {

    @Test
    public void checkString() {
        assertTrue(Addons.checkString("asd",Addons.stringToSHA256("asd")));
    }
}