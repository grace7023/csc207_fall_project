package fall2018.csc2017.game_centre.twenty_forty;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TFBoardTest {

    /**
     * The game for testing
     */
    private TFGame game;

    /**
     * Make set of boxes in this preset:
     * 2 0 0
     * 0 4 0
     * 0 0 0
     * @return List of boxes in order
     */
    private List<Box> makeTwoBoxes() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            if (i == 0) {
                boxes.add(new Box(1));
            } else if (i == 4) {
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
     * Test whether getBox works
     */
    @Test
    public void getBox() {
        setUpGame(makeFilledBoxes());
        assertEquals(2, game.getBoard().getBox(0,1 ).getExponent());
        assertEquals(1, game.getBoard().getBox(0,0 ).getExponent());
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
        setUpGame(makeTwoBoxes());
        game.getBoard().moveBoxesUp();
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
        setUpGame(makeTwoBoxes());
        game.getBoard().moveBoxesDown();
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
        setUpGame(makeTwoBoxes());
        game.getBoard().moveBoxesLeft();
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
        setUpGame(makeTwoBoxes());
        game.getBoard().moveBoxesRight();
        assertEquals(1, game.getBoard().getBox(0,2 ).getExponent());
        assertEquals(2, game.getBoard().getBox(1,2 ).getExponent());
    }

    /**
     * Test whether becomeBoard works
     */
    @Test
    public void becomeBoard() {
        TFBoard filledBoard = new TFBoard(makeFilledBoxes(), 3);
        setUpGame(makeTwoBoxes());
        game.getBoard().becomeBoard(filledBoard);
        for (int i = 0; i < 3 * 3; i++) {
            int row = i / 3;
            int col = i % 3;
            assertEquals(filledBoard.getBox(row,col).getExponent(), game.getBoard().getBox(row,col).getExponent());
        }
    }

    /**
     * Test whether getNumBoxes works
     */
    @Test
    public void getNumBoxes() {
        setUpGame(makeTwoBoxes());
        assertEquals(9,game.getBoard().getNumBoxes());
    }

    /**
     * Combine Boxes to the left
     * From:
     * 1 1 0
     * 0 0 0
     * 0 0 0
     * To:
     * 2 0 0
     * 0 0 0
     * 0 0 0
     */
    @Test
    public void combineBoxesLeft() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            if (i == 0 || i == 1) {
                boxes.add(new Box(1));
            } else {
                boxes.add(new Box(0));
            }
        }
        setUpGame(boxes);
        assertEquals(1, game.getBoard().getBox(0, 0).getExponent());
        game.getBoard().moveBoxesLeft();
        assertEquals(2, game.getBoard().getBox(0, 0).getExponent());
    }

    /**
     * Combine Boxes to the right
     * From:
     * 0 1 1
     * 0 0 0
     * 0 0 0
     * To:
     * 0 0 2
     * 0 0 0
     * 0 0 0
     */
    @Test
    public void combineBoxesRight() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            if (i == 1 || i == 2) {
                boxes.add(new Box(1));
            } else {
                boxes.add(new Box(0));
            }
        }
        setUpGame(boxes);
        assertEquals(1, game.getBoard().getBox(0, 2).getExponent());
        game.getBoard().moveBoxesRight();
        assertEquals(2, game.getBoard().getBox(0, 2).getExponent());
    }

    /**
     * Combine Boxes to upwards
     * From:
     * 1 0 0
     * 1 0 0
     * 0 0 0
     * To:
     * 2 0 0
     * 0 0 0
     * 0 0 0
     */
    @Test
    public void combineBoxesUp() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            if (i == 0 || i == 3) {
                boxes.add(new Box(1));
            } else {
                boxes.add(new Box(0));
            }
        }
        setUpGame(boxes);
        assertEquals(1, game.getBoard().getBox(0, 0).getExponent());
        game.getBoard().moveBoxesUp();
        assertEquals(2, game.getBoard().getBox(0, 0).getExponent());
    }

    /**
     * Combine Boxes to downwards
     * From:
     * 1 0 0
     * 1 0 0
     * 0 0 0
     * To:
     * 0 0 0
     * 0 0 0
     * 2 0 0
     */
    @Test
    public void combineBoxesDown(){
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            if (i == 0 || i == 3) {
                boxes.add(new Box(1));
            } else {
                boxes.add(new Box(0));
            }
        }
        setUpGame(boxes);
        assertEquals(1,game.getBoard().getBox(0,0).getExponent());
        game.getBoard().moveBoxesDown();
        assertEquals(2,game.getBoard().getBox(2,0).getExponent());
    }

    /**
     * Test isFull works
     */
    @Test
    public void isFull(){
        setUpGame(makeFilledBoxes());
        assertEquals(1,game.getBoard().getBox(0,0).getExponent());
        game.getBoard().moveBoxesUp();
        assertEquals(1,game.getBoard().getBox(0,0).getExponent());
    }
}