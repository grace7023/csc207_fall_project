package fall2018.csc2017.game_centre.sliding_tiles;

import fall2018.csc2017.game_centre.*;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class SlidingTilesGame extends Game implements Serializable {

    /**
     * The size of the board.
     */
    private int boardSize;
    /**
     * The board being managed.
     */
    private STBoard board;

    /**
     * Temporary board for proper shuffling
     */
    private STBoard tempBoard;
    /**
     * The moves made since the start of the game
     */
    private List<Integer> moves;

    /**
     * The score of the current game
     */
    private int score;

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
     * Game description
     */
    public final static String GAME_DESC = "Welcome To Sliding Tiles \n A Puzzle Game where you" +
                        " must arrange the numbers in the correct order";

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    public SlidingTilesGame(STBoard board) {
        this.boardSize = board.getBoardSize();
        this.board = board;
        this.score = 0;
        this.moves = new ArrayList<>();
        GestureDetectGridView.detectFling = false;
    }

    /**
     * Manage a board given the size of the board.
     *
     * @param boardSize the board size
     */

    public SlidingTilesGame(int boardSize) {
        this.boardSize = boardSize;
        this.score = 0;
        GestureDetectGridView.detectFling = false;
        List<STTile> tiles = new ArrayList<>();
        moves = new ArrayList<>();
        final int numTiles = this.boardSize * this.boardSize;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new STTile(tileNum, boardSize));
        }
        tempBoard = new STBoard(tiles, boardSize);
        shuffleTiles();
        this.board = tempBoard;
    }

    /**
     * Return the size of the board
     *
     * @return boardSize
     */
    public int getScreenSize() {
        return boardSize;
    }

    /**
     * Return game over text according winning or losing
     * @return "YOU WIN!"
     */
    public String gameOverText() {
        return "YOU WIN!";
    }

    /**
     * Return the current board.
     */
    public STBoard getBoard() {
        return board;
    }

    /**
     * Return the position of the blank tile.
     *
     * @param board the board which the blank tile is from.
     * @return position of the blank tile
     */
    private int returnTilePosition(STBoard board, int tileId) {
//        int id = board.numTiles();
        int counter = 0;
        for (int row = 0; row != this.boardSize; row++) {
            for (int col = 0; col != this.boardSize; col++) {
                STTile cur = board.getTile(row, col);
                if (cur.getId() != tileId)
                    counter++;
                else {
                    return counter;
                }
            }
        }
        return -1;
    }

    /**
     * Shuffle tiles to make sure game is solvable
     */
    private void shuffleTiles() {
        Random rand = new Random();
        int blankId = tempBoard.getBoardSize() * tempBoard.getBoardSize();
        for (int i = 0; i < Math.pow(this.boardSize, 4); i++) {
            int blankTilePosition = returnTilePosition(tempBoard, blankId);
            int randomNumber = rand.nextInt(4);
            randomSwap(randomNumber, blankTilePosition);
        }

    }

    /**
     * Swap to random tiles
     *
     * @param randomNumber      a randomly generated number
     * @param blankTilePosition the position of the blank tile
     */

    private void randomSwap(int randomNumber, int blankTilePosition) {
        List<STTile> adjacentTiles = adjacentTiles(blankTilePosition, tempBoard);
        STTile swappingTile = adjacentTiles.get(randomNumber);
        if (!(swappingTile == null)) {
            int swappingTilePos = returnTilePosition(tempBoard, swappingTile.getId());
            int row1 = blankTilePosition / boardSize;
            int col1 = blankTilePosition % boardSize;
            int row2 = swappingTilePos / boardSize;
            int col2 = swappingTilePos % boardSize;
            tempBoard.swapTiles(row1, col1, row2, col2);
        }

    }

    /**
     * Tells whether the tiles are in order.
     *
     * @return True iff the tiles of board are in ascending row-major order
     */
    public boolean puzzleSolved() {
        int trackingId = 1;
        for (STTile cur : board) {
            if (cur.getId() != trackingId) {
                return false;
            }
            trackingId++;
        }
        // No out-of-order tiles found
        return true;
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
    public boolean isValidMove(int arg) {
        List<STTile> surroundings = adjacentTiles(arg, this.board);
        STTile blank = returnBlankTile();
        return surroundings.contains(blank);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param arg the position
     */
    public void move(int arg) {
        int row = arg / this.boardSize;
        int col = arg % this.boardSize;
        List<STTile> adjacent_tiles = adjacentTiles(arg, this.board);
        STTile blank = returnBlankTile();
        int indBlank = adjacent_tiles.indexOf(blank);
        int row2 = row + MOVE_ADJUSTMENT_VALUES[indBlank][0];
        int col2 = col + MOVE_ADJUSTMENT_VALUES[indBlank][1];
        score++;
        board.swapTiles(row, col, row2, col2);
        addMove(indBlank, arg);
    }

    /**
     * Add the position to moves that would perform the opposite of the previous move.
     * if indBlank = 0, then the previous move was the moving up.
     * * if indBlank = 1, then the previous move was moving down.
     * * if indBlank = 2, then the previous move was moving left.
     * * if indBlank = 3, then the previous move was moving right.
     *
     * @param indBlank whether the blank tile was above, below, left or right of the tile.
     * @param position position of non-blank tile
     */

    private void addMove(int indBlank, int position) {
        int[] moveAdjustment = {-this.boardSize, this.boardSize, -1, 1};
        moves.add(0, position + moveAdjustment[indBlank]);
    }

    /**
     * Return the blank tile.
     *
     * @return the blank tile
     */

    private STTile returnBlankTile() {
        int blankId = board.getBoardSize() * board.getBoardSize();
        for (STTile t : board) {
            if (t.getId() == blankId) {
                return t;
            }
        }
        // The code will not reach here, since there always exists a blank tile in this.board.tiles
        return null;
    }


    /**
     * Return a ArrayList of the adjacent tiles of the given tile. The adjacent tiles are
     * given in this order: above, below, left, right.
     *
     * @param position the position of the tile
     * @return ArrayList of adjacent tiles
     */

    private List<STTile> adjacentTiles(int position, STBoard board) {
        int row = position / this.boardSize;
        int col = position % this.boardSize;
        STTile above = row == 0 ? null : board.getTile(row - 1, col);
        STTile below = row == this.boardSize - 1 ? null : board.getTile(row + 1, col);
        STTile left = col == 0 ? null : board.getTile(row, col - 1);
        STTile right = col == this.boardSize - 1 ? null : board.getTile(row, col + 1);
        return Arrays.asList(above, below, left, right);
    }

    /**
     * Undoes the latest move made
     */
    void undo() {
        int undoPosition = moves.get(0);
        move(undoPosition);
        // remove undo move just performed
        moves.remove(0);
        if (moves.get(0) == undoPosition) {
            moves.remove(0);
        }
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
     * Return whether the undo function is valid based on the current state.
     *
     * @return whether the game can undo a move
     */
    boolean canUndoMove() {
        return moves.size() > 0;
    }

    /**
     * Changes activity from game to settings
     *
     * @param PackageContext game activity context
     * @return SlidingTilesSettings a
     */
    @Override
    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, SlidingTilesSettings.class);
    }

    /**
     * Return GameActivity intent
     * @param PackageContext game activity context
     * @return Intent of Game Activity
     */
    @Override
    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        return new Intent(PackageContext, SlidingTilesGameActivity.class);
    }
}
