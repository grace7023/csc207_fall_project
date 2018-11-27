package fall2018.csc2017.game_centre.twenty_forty;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.game_centre.Game;
import fall2018.csc2017.game_centre.GestureDetectGridView;

public class TFGame extends Game implements Serializable {

    private final int INIT_BOX_NUM = 2;

    private int boardSize;

    private TFBoard board;

    private ArrayList<TFBoard> pastBoards;

    private int score;

    public final static String GAME_DESC = "Welcome To 2048 \n Swipe the screen to slide the " +
            "numbered tiles around. Merge like-numbered tiles to add their numbers together. " +
            "Get the 2048 tile to win!";

    /**
     * HashMap which lets the code associate the arg in TFGame.move(arg) with an actual move rather
     * than a magic number
     */
/*    static final HashMap<String, Integer> MOVE_ARG;

    static {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("Up", 1);
        map.put("Down", 2);
        map.put("Left", 3);
        map.put("Right", 4);
        MOVE_ARG = map;
    }*/

    public TFGame(TFBoard board) {
        this.boardSize = board.getBoardSize();
        this.board = board;
        this.score = 0;
    }

    public TFGame(int boardSize) {
        this.boardSize = boardSize;
        this.score = 0;
        GestureDetectGridView.detectFling = true;
        //TODO: moves ?

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
            int exponent = 2 * (rng.nextInt(2) + 1);
            boxes.set(pos, new Box(exponent));
        }

        this.board = new TFBoard(boxes, this.boardSize);

    }

    public TFBoard getBoard() {
        return board;
    }

    public int getScore() { //TODO: Figure out score counting system, might need public static setScore()  also
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
        // if no block can be moved then return false
        //TODO: implement isValidMove
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
        ArrayList<Box> boxesSave = GetBoxList(board);
        pastBoards.add(new TFBoard(boxesSave, this.board.getBoardSize()));
        if (arg == GestureDetectGridView.MOVE_ARG.get("Up")) {
            addedScore = board.moveBoxesUp();
        } else if (arg == GestureDetectGridView.MOVE_ARG.get("Down")) {
            addedScore = board.moveBoxesDown();
        } else if (arg == GestureDetectGridView.MOVE_ARG.get("Left")) {
            addedScore = board.moveBoxesLeft();
        } else if (arg == GestureDetectGridView.MOVE_ARG.get("Right")) {
            addedScore = board.moveBoxesRight();
        }
        score += addedScore;
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

    public String gameOverText() {
        return "GAME OVER!";
    }

    @Override
    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, TFSettings.class);
    }

    @Override
    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, TFGameActivity.class);
    }
}
