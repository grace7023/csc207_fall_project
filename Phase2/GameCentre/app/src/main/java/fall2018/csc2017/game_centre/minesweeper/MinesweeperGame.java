package fall2018.csc2017.game_centre.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import fall2018.csc2017.game_centre.Game;
import fall2018.csc2017.game_centre.GestureDetectGridView;


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

    private boolean flagging;

    private boolean bombClicked;

    // TODO: add game description
    public final static String GAME_DESC = "";

    private Date startTime;


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
        GestureDetectGridView.detectFling = false;
        bombsLeft = numBombs;
        flagging = false;
        bombClicked = false;
        startTime = Calendar.getInstance().getTime();

        generateBoard();
    }

    /**
     * Creates the board with the specified rows, columns and number of bombs
     */
    private void generateBoard() {
        List<Tile> tiles = new ArrayList<>(numRows*numCols);
        int bombs = 0;
        for (int i = 0; i < getScreenSize(); i++) {
            if (bombs < numBombs) {
                tiles.add(new Tile(Tile.MINE));
                bombs++;
            } else {
                tiles.add(new Tile(Tile.EMPTY));
            }
        }
        Collections.shuffle(tiles);
        board = new Board(tiles, numRows, numCols);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int position = i * numCols + j;
                if (board.getTile(i, j).getId() != Tile.MINE) {
                    int adjacentBombs = 0;
                    for (Tile neighbor : adjacentTiles(position, board)) {
                        if (neighbor != null && neighbor.getId() == Tile.MINE) {
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
        return puzzleSolved() || bombClicked;
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
            if (board.getTile(row,col).isFlagged()) {
                bombsLeft++;
            } else {
                bombsLeft--;
            }
            board.toggleFlag(row, col);
        } else {
            if (board.getTile(row,col).getId() == Tile.MINE) {
                    bombClicked = true;
            } else {
                board.addNumRevealed();
            }
            if (board.getTile(row,col).getId() == Tile.EMPTY) {
                expandEmpty(row, col);
            }
            board.revealTile(row, col);

        }
    }

    // TODO: implement expandEmpty either recursively or iteratively
    private void expandEmpty(int row, int col) {
        // this doesn't work someone else fix it, how do you make it not loop endlessly
        int position = row * numCols + col;
        List<Tile> neighbors = adjacentTiles(position, board);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i != 0 || j != 0) {
                    Tile neighbor = neighbors.get((i+1)*3 + (j+1));
                    if (neighbor != null && neighbor.getId() == Tile.EMPTY && !neighbor.isRevealed()) {
                        revealAdjacent(row, col);
                        expandEmpty(row+i, col+j);
                    }
                }
            }
        }

    }

    /**
     * Reveals all adjacent tiles to the tile at row, col
     *
     * @param row The row where the tile is on the board
     * @param col The column where the tile is on the board
     */
    private void revealAdjacent(int row, int col) {
        int position = row * numCols + col;
        List<Tile> neighbors = adjacentTiles(position, board);
        for (Tile t : neighbors)
            board.revealTile(t);
    }
    /**
     * Return a ArrayList of the adjacent tiles of the given tile. The adjacent tiles are
     * given in this order: upLeft, above, upRight, left, null, right, downLeft, below, downRight.
     *
     * @param position the position of the tile
     * @return ArrayList of adjacent tiles
     */

    private List<Tile> adjacentTiles(int position, Board board) {
        int row = position / this.numRows;
        int col = position % this.numCols;
        Tile upLeft = row == 0 || col == 0 ? null : board.getTile(row - 1, col - 1);
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile upRight = row == 0 || col == this.numCols - 1 ? null: board.getTile(row-1, col+1);
        Tile below = row == this.numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == this.numCols - 1 ? null : board.getTile(row, col + 1);
        Tile downLeft = row == this.numRows - 1 || col == 0? null : board.getTile(row + 1, col - 1);
        Tile downRight = row == this.numRows - 1 || col == this.numCols - 1 ? null : board.getTile(row + 1, col + 1);
        return Arrays.asList(upLeft, above, upRight, left, null, right, downLeft, below, downRight);
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

    public String getTime(){

        int currentTimeSeconds = Calendar.getInstance().getTime().getSeconds() - startTime.getSeconds();
        int currentTimeMinutes = Calendar.getInstance().getTime().getMinutes() - startTime.getMinutes();

        return "Time: " + String.valueOf(currentTimeMinutes) + ":" + String.valueOf(currentTimeSeconds);
    }

    public int getNumBombs(){ return bombsLeft; }

    public int getNumRows() { return numRows; }
    public int getNumCols() { return numCols; }
}
