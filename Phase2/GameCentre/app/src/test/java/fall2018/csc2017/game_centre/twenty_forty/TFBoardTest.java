package fall2018.csc2017.game_centre.twenty_forty;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TFBoardTest {

    private TFGame game;

    /**
     * 2 0 0
     * 0 4 0
     * 0 0 0
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
     * 2 4 8
     * 16 32 64
     * 128 256 512
     */
    private List<Box> makeFilledBoxes() {
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < 3 * 3; i++) {
            boxes.add(new Box(i + 1));
        }
        return boxes;
    }

    private void setUpGame(List<Box> boxes){
        TFBoard board = new TFBoard(boxes, 3);
        game = new TFGame(board);
    }

    @Test
    public void getBox() {
        setUpGame(makeFilledBoxes());
        assertEquals(2, game.getBoard().getBox(0,1 ).getExponent());
        assertEquals(1, game.getBoard().getBox(0,0 ).getExponent());
    }

    /**
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

    @Test
    public void getNumBoxes() {
        setUpGame(makeTwoBoxes());
        assertEquals(9,game.getBoard().getNumBoxes());
    }

    /**
     * 1 1 0
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
    @Test
    public void isFull(){
        setUpGame(makeFilledBoxes());
        assertEquals(1,game.getBoard().getBox(0,0).getExponent());
        game.getBoard().moveBoxesUp();
        assertEquals(1,game.getBoard().getBox(0,0).getExponent());
    }
}