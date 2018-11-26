package fall2018.csc2017.game_centre.twenty_forty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fall2018.csc2017.game_centre.Game;

public class TFGame extends Game implements Serializable {

    private int boardSize;

    private TFBoard board;

    private ArrayList<TFBoard> pastBoards;

    private int score;

    public final static String GAME_DESC = "Welcome To 2048 \n Join the numbers" +
            " and get to the 2048 tile! \n\n  Swipe to move all tiles. " +
            "When two tiles with the same number touch, they merge into one.";

    /**
     * HashMap which lets the code associate the arg in TFGame.move(arg) with an actual move rather
     * than a magic number
     */
    static final HashMap<String, Integer> MOVE_ARG;

    static {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Up", 1);
        map.put("Down", 2);
        map.put("Left", 3);
        map.put("Right", 4);
        MOVE_ARG = map;
    }


    public TFGame(TFBoard board) {
//        this.boardSize = TFBoard.getBoardSize(); TODO: static ???
        this.board = board;
        this.score = 0;
    }

    public TFGame(int boardSize) {
        this.boardSize = boardSize;
        this.score = 0;
        //TODO: moves ?
        List<Box> boxes = new ArrayList<>();

        // generate 2 random boxes
        Random initialBoxes = new Random();
        int coorX1 = initialBoxes.nextInt(boardSize);
        int coorY1 = initialBoxes.nextInt(boardSize); //TODO: may need to make this look better
        int coorX2 = initialBoxes.nextInt(boardSize);
        int coorY2 = initialBoxes.nextInt(boardSize);

        while (coorX1 == coorX2 && coorY1 == coorY2) {
            coorX2 = initialBoxes.nextInt(boardSize);
            coorY2 = initialBoxes.nextInt(boardSize);
        }

        for (int coorX = 0; coorX != boardSize; coorX++) {
            for (int coorY = 0; coorY != boardSize; coorY++) {
                if (coorX == coorX1 && coorY == coorY1) {
                    boxes.add(new Box(2)); //TODO: 2 or 4. currently default 2
                } else if (coorX == coorX2 && coorY == coorY2) {
                    boxes.add(new Box(2)); //TODO: 2 or 4. currently default 2
                } else {
                    boxes.add(new Box(0));
                }
            }
        }
        this.board = new TFBoard(boxes, this.boardSize);

    }

    public TFBoard getBoard() {
        return board;
    }

    public int getScore() {
        return score;
    }


    /**
     * Return whether a move with parameter arg is valid
     *
     * @param arg what argument representing the move attempted
     * @return whether the tap is valid
     */
    @Override
    public boolean isValidMove(int arg) {
        return false;
    }

    /**
     * Perform a move identified by arg.
     *
     * @param arg integer identifying the move
     */
    @Override
    public void move(int arg) {
        ArrayList<Box> boxesSave = GetBoxList(board);
        pastBoards.add(new TFBoard(boxesSave, this.board.getBoardSize()));

        switch (arg) {
            case 1:
                board.MoveBoxesUp();
                break;
            case 2:
                board.MoveBoxesDown();
                break;
            case 3:
                board.MoveBoxesLeft();
                break;
            case 4:
                board.MoveBoxesRight();
                break;
        }
    }

    /**
     * Return whether the Game is over.
     *
     * @return whether Game is over
     */
    @Override
    public boolean isOver() {
        return isStuck();
    }

    /**
     * Tells whether the tiles are stuck
     *
     * @return True iff tiles are not movable
     */
    private boolean isStuck() {
        //TODO: implement isStuck()
        return true;
    }

    /**
     * Undo function that reverses the most recent move.
     */
    void undo() {
        this.board = pastBoards.remove(0); //TODO: check if getting first board is the previous save
    }

    boolean canUndoMove() {
        return this.pastBoards.size() != 0;
    }

    /**
     * Size of the relevant screen real estate for game.
     */
    @Override
    public int getScreenSize() {
        return boardSize;
    }

    private ArrayList<Box> GetBoxList(TFBoard board) {
        ArrayList<Box> boxes = new ArrayList<>();
        for (Box box : board) {
            boxes.add(box);
        }
        return boxes;
    }
}
