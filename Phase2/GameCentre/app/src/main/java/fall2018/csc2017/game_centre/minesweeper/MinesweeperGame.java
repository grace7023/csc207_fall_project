package fall2018.csc2017.game_centre.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.game_centre.Game;


/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class MinesweeperGame extends Game implements Serializable {
    //TODO: figure out how to count score as time passed
    /**
     * The number of rows of the board.
     */
    private int numRows;

    /**
     * The number of columns of the board.
     */
    private int numCols;

    /**
     * The number of columns of the board.
     */
    private int numBombs;

    /**
     * The number of bombs left on the board.
     */
    private int bombsLeft;

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * The score of the current game
     */
    private int score;

    private boolean flagging;
    /**
     * Given the row and column of a tile, this list indicates how to adjust those two numbers
     * to obtain the row and column of an adjacent tile.
     * MOVE_ADJUSTMENT_VALUES[0] - adjustment for tile above
     * MOVE_ADJUSTMENT_VALUES[1] - adjustment for tile below
     * MOVE_ADJUSTMENT_VALUES[2] - adjustment for tile left
     * MOVE_ADJUSTMENT_VALUES[3] - adjustment for tile right
     * each internal array is of the format {row adjustment, column adjustment}. For example, given
     * row and column of a tile, to get the row and column of the tile above, row would add -1 and
     * column would add 0.
     */
    private final static int[][] MOVE_ADJUSTMENT_VALUES = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */

    /** TODO: figure out if we need this
    public MinesweeperGame(Board board) {
        this.numRows = board.getBoardSize();
        this.board = board;
        this.score = 0;
    }
    */

    /**
     * Manage a board given the size of the board.
     *
     * @param numRows the number of rows of this board
     * @param numCols the number of columns of this board
     * @param numBombs the number of bombs of this board
     */

    public MinesweeperGame(int numRows, int numCols, int numBombs) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.numBombs = numBombs;
        this.bombsLeft = numBombs;
        this.score = 0;
        this.flagging = false;

        generateBoard();
    }

    /**
     * Creates the board with the specified rows, columns and number of bombs
     */
    private void generateBoard() {
        List<Tile> tiles = new ArrayList<>();
        int bombs = 0;
        for (int i = 0; i < tiles.size(); i++) {
            if (bombs < numBombs) {
                tiles.add(new Tile(Tile.BOMB));
                bombs++;
            } else {
                tiles.add(new Tile(Tile.EMPTY));
            }
        }
        Collections.shuffle(tiles);
        board = new Board(tiles, numRows, numCols);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int position = i * numRows + j;
                if (board.getTile(i, j).getId() != Tile.BOMB) {
                    int adjacentBombs = 0;
                    for (Tile neighbor : adjacentTiles(position, board)) {
                        if (neighbor.getId() == Tile.BOMB) {
                            adjacentBombs++;
                        }
                    }
                    board.getTile(i, j).setId(adjacentBombs);
                }
            }
        }
    }

    /**
     * Return the size of the board
     *
     * @return boardSize
     */
    public int getScreenSize() {
        return numRows * numCols;
    }

    /**
     * Return the current board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Tells whether the tiles are in order.
     *
     * @return True iff the tiles of board are in ascending row-major order
     */
    public boolean puzzleSolved() {
        return board.getNumRevealed() >= getScreenSize() - numBombs;
    }

    /**
     * Return whether the current state of the game is over.
     *
     * @return whether the game is over
     */

    public boolean isOver() {
        return puzzleSolved();
    }


    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param arg the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    // TODO: implement the isValidMove method in MinesweeperGame
    public boolean isValidMove(int arg) {
        int row = arg / numRows;
        int col = arg % numCols;
        return !board.getTile(row, col).isRevealed();
    }

    /**
     * Turns the current move to either placing a flag or revealing a tile
     */
    public void toggleFlagging() {
        flagging = !flagging;
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param arg the position
     */
    public void move(int arg) {
        int row = arg / numRows;
        int col = arg % numCols;
        if (flagging) {
            board.getTile(row,col).toggleFlag();
            if (board.getTile(row,col).isFlagged()) {
                bombsLeft--;
            } else {
                bombsLeft++;
            }
        } else {
            board.getTile(row,col).reveal();
        }
    }

    /**
     * Return a ArrayList of the adjacent tiles of the given tile. The adjacent tiles are
     * given in this order: upLeft, above, upRight, below, left, right, downLeft, downRight.
     *
     * @param position the position of the tile
     * @return ArrayList of adjacent tiles
     */

    private List<Tile> adjacentTiles(int position, Board board) {
        int row = position / this.numRows;
        int col = position % this.numCols;
        Tile upLeft = row == 0 && col == 0 ? null : board.getTile(row - 1, col - 1);
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile upRight = row == 0 && col == this.numCols - 1 ? null : board.getTile(row - 1, col + 1);
        Tile below = row == this.numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == this.numCols - 1 ? null : board.getTile(row, col + 1);
        Tile downLeft = row == this.numRows - 1 && col == 0? null : board.getTile(row + 1, col - 1);
        Tile downRight = row == this.numRows - 1 && col == this.numCols - 1 ? null : board.getTile(row + 1, col + 1);
        return Arrays.asList(upLeft, above, upRight, below, left, right, downLeft, downRight);
    }

    /**
     * Return the score
     *
     * @return The score of the current game
     */
    int getScore() {
        return score;
    }

    /**
     * Changes activity from game to settings
     *
     * @param PackageContext game activity context
     * @return SlidingTilesSettings a
     */

    @Override
    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, MinesweeperSettings.class);
    }

    @Override
    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, MinesweeperGameActivity.class);
    }
}
