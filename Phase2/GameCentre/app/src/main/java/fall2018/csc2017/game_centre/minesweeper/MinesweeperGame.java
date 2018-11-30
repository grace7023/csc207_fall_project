package fall2018.csc2017.game_centre.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fall2018.csc2017.game_centre.Game;
import fall2018.csc2017.game_centre.GestureDetectGridView;


/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class MinesweeperGame extends Game implements Serializable {
    /**
     * The number of rows of the board.
     */
    private int numRows;

    /**
     * The number of columns of the board.
     */
    private int numCols;

    /**
     * The number of bombs on the board.
     */
    private int numBombs;

    /**
     * The number of bombs left on the board. Represents numBombs - number of Flagged tiles
     */
    private int totalFlagged;

    /**
     * The board being managed.
     */
    private MSBoard board;

    /**
     * Whether the game is currently in flagging mode or not
     */
    private boolean flagging;

    /**
     * Is true if a tile that was a bomb was clicked
     */
    private boolean bombClicked;

    /**
     * Displayed game description in the GameMenu
     */
    public final static String GAME_DESC = "Welcome to Minesweeper!\nTap on tiles to reveal them\n" +
            "Tap the flag button to flag bombs\nReveal all the non-bomb tiles on the field to win!";

    /**
     * Keeps track of the elapsed time since this MinesweeperGame object was created
     */
    private long startTime;
    /** Keeps track of the time when quitting a game */
    private long saveTime;
    /** Keeps track of the time when loading a game */
    private long loadTime;


    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    MinesweeperGame(MSBoard board, int numBombs) {
        this.numRows = board.getNumRows();
        this.numCols = board.getNumCols();
        this.board = board;
        this.numBombs = numBombs;
        flagging = false;
        bombClicked = false;
        startTime = System.nanoTime();
        saveTime = startTime;
    }

    /**
     * Manage a board given the size of the board and the number of bombs.
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
        totalFlagged = numBombs;
        flagging = false;
        bombClicked = false;

        generateBoard();
        startTime = System.nanoTime();
        saveTime = startTime;
    }

    /**
     * Creates and randomly scrambles the board with the specified number of rows, columns and bombs
     */
    private void generateBoard() {
        List<MSTile> tiles = new ArrayList<>(numRows*numCols);
        int bombs = 0;
        for (int i = 0; i < getScreenSize(); i++) {
            if (bombs < numBombs) {
                tiles.add(new MSTile(MSTile.MINE));
                bombs++;
            } else {
                tiles.add(new MSTile(MSTile.EMPTY));
            }
        }
        Collections.shuffle(tiles);
        board = new MSBoard(tiles, numRows, numCols);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int position = i * numCols + j;
                if (board.getTile(i, j).getId() != MSTile.MINE) {
                    int adjacentBombs = 0;
                    for (MSTile neighbour : adjacentTiles(position, board)) {
                        if (neighbour != null && neighbour.getId() == MSTile.MINE) {
                            adjacentBombs++;
                        }
                    }
                    board.getTile(i, j).setId(adjacentBombs);
                }
            }
        }
    }

    /**
     * Returns the size of the board
     *
     * @return screenSize
     */
    public int getScreenSize() {
        return numRows * numCols;
    }

    /**
     * Returns the current board.
     */
    public MSBoard getBoard() {
        return board;
    }

    /**
     * Return whether the current state of the game is over either if a bomb was clicked or the game is completed.
     *
     * @return whether the game is over
     */

    public boolean isOver() {
        if (bombClicked)
            return true;
        else
            return puzzleSolved();
    }

    boolean puzzleSolved() {
        return numCols * numRows - board.getNumRevealed() == numBombs;
    }


    /**
     * Return whether there are any unrevealed tiles around the selected tile at position arg
     *
     * @param arg the tile to check
     * @return whether the tile at position has any unrevealed tiles around it
     */
    public boolean isValidMove(int arg) {
        int row = arg / numRows;
        int col = arg % numCols;
        int numRevealed = 0;
        int numNeighbours = 8;
        if (board.getTile(row, col).isRevealed()) {
            List<MSTile> neighbours = adjacentTiles(arg, board);
            for (MSTile neighbour : neighbours) {
                if (neighbour == null) {
                    numNeighbours--;
                } else if (neighbour.isRevealed()) {
                    numRevealed++;
                }
            }
        }
        return numRevealed != numNeighbours;
    }

    /**
     * Turns the current move to either placing a flag or revealing a tile
     */
    void toggleFlagging() {
        flagging = !flagging;
    }

    /**
     * Process a touch at position in the board, revealing or flagging tiles as appropriate.
     *
     * @param arg the position
     */
    public void move(int arg) {
        int row = arg / numRows;
        int col = arg % numCols;
        MSTile currentTile = board.getTile(row, col);
        int currentTileId = currentTile.getId();
        if (flagging) {
            flaggingMove(currentTile, arg);
        } else {
            revealingMove(currentTile, currentTileId);
        }
        if (currentTile.isRevealed() && currentTileId != MSTile.EMPTY){
            revealedTileMove(currentTileId, arg);
        }
    }

    /**
     * Either flags an unflagged unrevealed tile or unflags a flagged unrevealed tile
     *
     * @param currentTile the current tile being clicked
     * @param currentTilePosition the position of the current tile being clicked
     */
    private void flaggingMove(MSTile currentTile, int currentTilePosition) {
        if (!currentTile.isRevealed()) {
            boolean currentTileFlagged = currentTile.isFlagged();
            totalFlagged = currentTileFlagged ? totalFlagged + 1 : totalFlagged - 1;
            board.toggleFlag(currentTilePosition / numRows, currentTilePosition % numCols);
        }
    }

    /**
     * Reveals the tile that is being clicked and sets bombClicked to true if the tile was a bomb
     *
     * @param currentTile the current tile being clicked
     * @param currentTileId the ID of the current tile being clicked
     */
    private void revealingMove(MSTile currentTile, int currentTileId) {
        if (!currentTile.isRevealed()) {
            if (currentTileId == MSTile.MINE) {
                bombClicked = true;
            } else if (currentTileId == MSTile.EMPTY) {
                expandEmpty(currentTile);
            }
            board.revealTile(currentTile);
        }
    }

    /**
     * Reveals all the unflagged tiles around a revealed tile if there are the same amount of flagged tiles as its own ID
     *
     * @param currentTileId the current tile being clicked's ID
     * @param currentTilePosition the position of the current tile being clicked
     */
    private void revealedTileMove(int currentTileId, int currentTilePosition) {
        int surroundingFlagged = 0;
        List<MSTile> neighbours = adjacentTiles(currentTilePosition, board);
        List<MSTile> emptyNeighbours = new ArrayList<>();
        for (MSTile t : neighbours) {
            if (t != null) {
                if (t.isFlagged())
                    surroundingFlagged++;
                if (t.getId() == MSTile.EMPTY)
                    emptyNeighbours.add(t);
            }
        }
        if (surroundingFlagged == currentTileId) {
            revealAdjacent(currentTilePosition / numRows, currentTilePosition % numCols);
            for (MSTile t : emptyNeighbours) {
                expandEmpty(t);
            }
        }
    }

    /**
     * Reveals all adjacent empty tiles and their adjacent empty tiles and so on
     *
     * @param original tile that we start expanding from
     */
    private void expandEmpty(MSTile original) {
        ArrayList<MSTile> queue = new ArrayList<>();

        queue.add(original);
        while (!queue.isEmpty()){
            MSTile current = queue.remove(0);
            List<MSTile> neighbours = adjacentTiles(board.getPosition(current), board);
            for (MSTile t : neighbours) {
                if (t != null && !t.isRevealed() && t.getId() == MSTile.EMPTY)
                    queue.add(t);
            }
            revealAdjacent(board.getPosition(current) / getNumRows(), board.getPosition(current) % getNumCols());
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
        List<MSTile> neighbours = adjacentTiles(position, board);
        for (MSTile t : neighbours)
            if (t != null && !t.isRevealed() && !t.isFlagged()) {
                if (t.getId() == MSTile.MINE) {
                    bombClicked = true;
                }
                board.revealTile(t);
            }
    }

    /**
     * Return a ArrayList of the adjacent tiles of the given tile. The adjacent tiles are
     * given in this order: upLeft, above, upRight, left, right, downLeft, below, downRight.
     *
     * @param position the position of the tile
     * @return ArrayList of adjacent tiles
     */

    private List<MSTile> adjacentTiles(int position, MSBoard board) {
        int row = position / this.numRows;
        int col = position % this.numCols;
        MSTile upLeft = row == 0 || col == 0 ? null : board.getTile(row - 1, col - 1);
        MSTile above = row == 0 ? null : board.getTile(row - 1, col);
        MSTile upRight = row == 0 || col == this.numCols - 1 ? null: board.getTile(row-1, col+1);
        MSTile below = row == this.numRows - 1 ? null : board.getTile(row + 1, col);
        MSTile left = col == 0 ? null : board.getTile(row, col - 1);
        MSTile right = col == this.numCols - 1 ? null : board.getTile(row, col + 1);
        MSTile downLeft = row == this.numRows - 1 || col == 0? null : board.getTile(row + 1, col - 1);
        MSTile downRight = row == this.numRows - 1 || col == this.numCols - 1 ? null : board.getTile(row + 1, col + 1);
        return Arrays.asList(upLeft, above, upRight, left, right, downLeft, below, downRight);
    }

    /**
     * Gets the intent to switch to the MinesweeperSettings activity
     *
     * @return the intent to MinesweeperSettings.class
     */
    @Override
    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, MinesweeperSettings.class);
    }

    /**
     * Gets the intent to switch to the MinesweeperGameActivity activity
     *
     * @return the intent to MinesweeperGameActivity.class
     */
    @Override
    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, MinesweeperGameActivity.class);
    }

    /**
     * Returns the amount of time since the start of the game
     *
     * @return The elapsed time as a string in the format "mm/ss"
     */
    // This code was adapted from an article by bealdung on 2018/11/28
    // URL: https://www.baeldung.com/java-measure-elapsed-time
    public String getTime(){
        int timer = (int)(((System.nanoTime() - startTime - (loadTime - saveTime))/1000000000));
        String min = timer / 60 < 10 ? "0" + String.valueOf(timer/60) : String.valueOf(timer/60);
        String sec = timer % 60 < 10 ? "0" + String.valueOf(timer%60) : String.valueOf(timer%60);

        return min + ":" + sec;
    }

    /**
     * Saves the the time when exitting from the game
     */
    void setSaveTime(){
        saveTime = System.nanoTime();
    }

    /**
     * Sets the time when loaded back into the game
     */
    void setLoadTime(){
        loadTime = System.nanoTime();
    }

    /**
     * Returns the number of bombs left on the board
     *
     * @return the number of bombs left
     */
    int getBombsLeft(){ return totalFlagged; }

    /**
     * Returns the number of rows of the board being managed
     *
     * @return the number of rows
     */
    int getNumRows() { return numRows; }

    /**
     * Returns the number of columns of the board being managed
     *
     * @return the number of columns
     */
    int getNumCols() { return numCols; }

    /**
     * Returns whether the game is either in revealing mode or flagging mode
     *
     * @return true if flagging, false if revealing
     */
    public boolean getFlagging() { return flagging; }

    /**
     * Return a string of whether the game is solved or over due to a bomb
     *
     * @return "GAME OVER!" if clicked on a bomb or "YOU WIN!" if the board was solved
     */
    public String gameOverText() {
        if (bombClicked) return "GAME OVER!";
        else return "YOU WIN!";
    }

}
