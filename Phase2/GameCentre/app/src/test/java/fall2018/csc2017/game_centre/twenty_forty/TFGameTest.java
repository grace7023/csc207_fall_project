package fall2018.csc2017.game_centre.twenty_forty;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TFGameTest {

    /**
     * The game for testing
     */
    private TFGame game;

    /**
     * Make set of boxes in this preset:
     * 2 0 0
     * 0 4 0
     * 0 0 0
     * @param first first location to set box with exponent 1
     * @param second second location to set box with exponent 2
     * @return List of boxes in order
     */
    private List<Box> makeTwoBoxes(int first, int second) {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            if (i == first) {
                boxes.add(new Box(1));
            } else if (i == second) {
                boxes.add(new Box(2));
            } else {
                boxes.add(new Box(0));
            }
        }
        return boxes;
    }

    /**
     * Make set of boxes in this preset
     * 2 4 8
     * 16 32 64
     * 128 256 512
     * @return list of boxes in order
     */
    private List<Box> makeFilledBoxes() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            boxes.add(new Box(i + 1));
        }
        return boxes;
    }

    /**
     * Configure game with desired set of boxes
     * @param boxes set of boxes to be set
     */
    private void setUpGame(List<Box> boxes){
        TFBoard board = new TFBoard(boxes, 3);
        game = new TFGame(board);
    }

    /**
     * Test whether getScore works
     */
    @Test
    public void getScore() {
        TFGame game = new TFGame(3);
        assertEquals(0,game.getScore());
    }

    /**
     * Test whether isValidMove works in 4 directions
     */
    @Test
    public void isValidMove() {
        setUpGame(makeTwoBoxes(0,1));
        assertFalse(game.isValidMove(1));
        assertTrue(game.isValidMove(2));
        assertFalse(game.isValidMove(3));
        assertTrue(game.isValidMove(4));
    }

    /**
     * Test whether moveBoxesUp works
     * From:
     * 2 0 0
     * 0 4 0
     * 0 0 0
     * To:
     * 2 4 0
     * 0 0 0
     * 0 0 0
     */
    @Test
    public void moveBoxesUp() {
        setUpGame(makeTwoBoxes(0,4));
        game.move(1);
        assertEquals(1, game.getBoard().getBox(0,0 ).getExponent());
        assertEquals(2, game.getBoard().getBox(0,1 ).getExponent());
    }

    /**
     * Test whether moveBoxesDown works
     * From:
     * 2 0 0
     * 0 4 0
     * 0 0 0
     * To:
     * 0 0 0
     * 0 0 0
     * 2 4 0
     */
    @Test
    public void moveBoxesDown() {
        setUpGame(makeTwoBoxes(0,4));
        game.move(2);
        assertEquals(1, game.getBoard().getBox(2,0 ).getExponent());
        assertEquals(2, game.getBoard().getBox(2,1 ).getExponent());
    }

    /**
     * Test whether moveBoxesLeft works
     * From:
     * 2 0 0
     * 0 4 0
     * 0 0 0
     * To:
     * 2 0 0
     * 4 0 0
     * 0 0 0
     */
    @Test
    public void moveBoxesLeft() {
        setUpGame(makeTwoBoxes(0,4));
        game.move(3);
        assertEquals(1, game.getBoard().getBox(0,0 ).getExponent());
        assertEquals(2, game.getBoard().getBox(1,0 ).getExponent());
    }

    /**
     * Test whether moveBoxesRight works
     * From:
     * 2 0 0
     * 0 4 0
     * 0 0 0
     * To:
     * 0 0 2
     * 0 0 4
     * 0 0 0
     */
    @Test
    public void moveBoxesRight() {
        setUpGame(makeTwoBoxes(0,4));
        game.move(4);
        assertEquals(1, game.getBoard().getBox(0,2 ).getExponent());
        assertEquals(2, game.getBoard().getBox(1,2 ).getExponent());
    }

    /**
     * Test whether isOver works, either board is stuck or 2048 shows up.
     */
    @Test
    public void isOver() {
        setUpGame(makeFilledBoxes());
        assertTrue(game.isOver());
        setUpGame(makeTwoBoxes(0,1));
        game.getBoard().getBox(0,1 ).setExponent(11);
        assertTrue(game.isOver());
    }

    /**
     * Test whether undo works
     */
    @Test
    public void undo() {
        setUpGame(makeTwoBoxes(0,1));
        game.move(2);
        assertEquals( 1,game.getBoard().getBox(2,0 ).getExponent());
        assertEquals( 2,game.getBoard().getBox(2,1 ).getExponent());
        game.undo();
        assertEquals( 1,game.getBoard().getBox(0,0 ).getExponent());
        assertEquals( 2,game.getBoard().getBox(0,1 ).getExponent());
    }

    /**
     * Test whether canUndoMove works, beginning of game and after making move
     */
    @Test
    public void canUndoMove() {
        setUpGame(makeTwoBoxes(0,1));
        assertFalse(game.canUndoMove());
        game.move(2);
        assertTrue(game.canUndoMove());
    }

    /**
     * Test whether getScreenSize works
     */
    @Test
    public void getScreenSize() {
        setUpGame(makeTwoBoxes(0,1));
        assertEquals(3, game.getScreenSize());
    }

    /**
     * Test whether gameOverText returns the correct test.
     * GAME OVER when lose - You won! when win
     */
    @Test
    public void gameOverText() {
        setUpGame(makeFilledBoxes());
        assertEquals("GAME OVER!", game.gameOverText());
        setUpGame(makeTwoBoxes(0,1 ));
        game.getBoard().getBox(0,0 ).setExponent(11);
        assertEquals("You won!", game.gameOverText());
    }

    /**
     * Test to see if getexponent == 0
     */
    @Test
    public void rng(){
        game = new TFGame(2);
    }
}