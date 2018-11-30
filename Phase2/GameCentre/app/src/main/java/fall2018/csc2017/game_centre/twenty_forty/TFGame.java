package fall2018.csc2017.game_centre.twenty_forty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.game_centre.Game;
import fall2018.csc2017.game_centre.GestureDetectGridView;

/**
 * Manage TwentyFortyGame
 */
public class TFGame extends Game implements Serializable {

    /**
     * Maximum number of boxes being randomly set
     */
    private final int INIT_BOX_NUM = 2;

    /**
     * The exponent that lead to victory
     */
    private final int WINNING_EXPONENT = 11;

    /**
     * The size of the board
     */
    private int boardSize;

    /**
     * The board being managed
     */
    private TFBoard board;

    /**
     * Previous boards created by making moves.
     */
    private List<TFBoard> pastBoards;

    /**
     * Previous scores modified by making moves
     */
    private List<Integer> pastScoreIncreases;

    /**
     * The score of the current game
     */

    private int score;

    /**
     * Game description
     */
    public final static String GAME_DESC = "Welcome To 2048\nSwipe the screen to slide the " +
            "numbered tiles around. Merge like-numbered tiles to add their numbers together. " +
            "Get the 2048 tile to win!";

    /**
     * Manage a board that has been pre-populated
     * @param board the board
     */
    public TFGame(TFBoard board) {
        this.boardSize = (int) Math.sqrt(board.getNumBoxes());
        this.board = board;
        this.pastBoards = new ArrayList<>();
        this.pastScoreIncreases = new ArrayList<>();
        GestureDetectGridView.detectFling = true;
        this.score = 0;
    }

    /**
     * Manage a board with given boardSize
     * @param boardSize
     */
    public TFGame(int boardSize) {
        this.boardSize = boardSize;
        this.pastBoards = new ArrayList<>();
        this.pastScoreIncreases = new ArrayList<>();
        this.score = 0;
        GestureDetectGridView.detectFling = true;
        List<Box> boxes = new ArrayList<>();
        for (int i = 0; i < this.boardSize * this.boardSize; i++) {
            boxes.add(new Box(0));
        }
        // Randomly inserting pre-existing items into boxes
        Random rng = new Random();
        for (int i = 0; i < INIT_BOX_NUM; i++) {
            int pos = rng.nextInt(boardSize * boardSize);
            while (boxes.get(pos).getExponent() != 0) {
                pos = rng.nextInt(boardSize * boardSize);
            }
            int exponent = rng.nextInt(2) + 1;
            boxes.set(pos, new Box(exponent));
        }
        this.board = new TFBoard(boxes, this.boardSize);
    }

    /**
     * Return board from game
     * @return board
     */
    public TFBoard getBoard() {
        return board;
    }

    /**
     * Return current score
     * @return
     */
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
        // Setting up movement direction to be checked
        int colMove = 0;
        int rowMove = 0;
        if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Up")) {
            rowMove = -1;
        } else if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Down")) {
            rowMove = 1;
        } else if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Left")) {
            colMove = -1;
        } else if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Right")) {
            colMove = 1;
        }
        // Return true if a non-empty box is found that can be moved into an empty space
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (0 <= row + rowMove && row + rowMove < boardSize && 0 <= col + colMove && col + colMove < boardSize) {
                    int curExponent = board.getBox(row, col).getExponent();
                    int adjExponent = board.getBox(row + rowMove, col + colMove).getExponent();
                    if (curExponent != 0 && (adjExponent == 0 || curExponent == adjExponent)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Perform a move identified by arg.
     *
     * @param arg integer identifying the move
     */
    @Override
    public void move(int arg) {
        int addedScore = 0;
        ArrayList<Box> boxesSave = getBoxList(board);
        pastBoards.add(new TFBoard(boxesSave, boardSize));
        if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Up")) {
            addedScore = board.moveBoxesUp();
        } else if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Down")) {
            addedScore = board.moveBoxesDown();
        } else if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Left")) {
            addedScore = board.moveBoxesLeft();
        } else if (arg == GestureDetectGridView.SWIPE_DIRECTION_ARG.get("Right")) {
            addedScore = board.moveBoxesRight();
        }
        score += addedScore;
        pastScoreIncreases.add(addedScore);
        board.update();
    }

    /**
     * Return whether the Game is over.
     *
     * @return whether Game is over
     */
    @Override
    public boolean isOver() {
        for (Box b : board) {
            if (b.getExponent() == WINNING_EXPONENT) return true;
        }
        return isStuck();
    }

    /**
     * Tells whether the tiles are stuck
     *
     * @return True iff tiles are not movable
     */
    private boolean isStuck() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int curExp = board.getBox(row, col).getExponent();
                if (curExp == 0) return false;
                for (Box neighbor : getNeighboringBoxes(row, col)) {
                    int neighborExp = neighbor.getExponent();
                    if (curExp == neighborExp) return false;
                }
            }
        }
        return true;
    }

    /**
     * Get all neighboring boxes from the current box
     * @param row row of Box
     * @param col column of Box
     * @return List of neighboring boxes
     */
    private ArrayList<Box> getNeighboringBoxes(int row, int col) {
        ArrayList<Box> neighbors = new ArrayList<>();
        if (row > 0) {
            neighbors.add(board.getBox(row - 1, col));
        }
        if (row < boardSize - 1) {
            neighbors.add(board.getBox(row + 1, col));
        }
        if (col > 0) {
            neighbors.add(board.getBox(row, col - 1));
        }
        if (col < boardSize - 1) {
            neighbors.add(board.getBox(row, col + 1));
        }
        return neighbors;
    }

    /**
     * Undo function that reverses the most recent move.
     */
    void undo() {
        this.board.becomeBoard(pastBoards.remove(pastBoards.size() - 1));
        this.score -= pastScoreIncreases.remove(pastScoreIncreases.size() - 1);
    }

    /**
     * Check if current game state permits undos
     * @return true if undoable
     */
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

    /**
     * Get the list of all of the Boxes in order
     * @param board
     * @return
     */
    private ArrayList<Box> getBoxList(TFBoard board) {
        ArrayList<Box> boxes = new ArrayList<>();
        for (Box box : board) {
            Box newBox = new Box(box.getExponent());
            boxes.add(newBox);
        }
        return boxes;
    }

    /**
     * Return game over text according winning or losing
     * @return "GAME OVER!" if lose; "You won!" if win
     */
    public String gameOverText() {
        if (isStuck()) return "GAME OVER!";
        return "You won!";
    }

    /**
     * Changes activity from game to Settings
     * @param PackageContext game activity context
     * @return Intent of Settings
     */
    @Override
    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, TFSettings.class);
    }

    /**
     * Return GameActivity intent
     * @param PackageContext game activity context
     * @return Intent of Game Activity
     */
    @Override
    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, TFGameActivity.class);
    }
}
