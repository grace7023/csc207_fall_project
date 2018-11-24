package fall2018.csc2017.game_centre.minesweeper;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * size of the board. numRows * numCols
     */
    private int numRows;
    private int numCols;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles;

    private int numRevealed;
    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == boardSize * boardSize
     * default values for NUM_ROWS and NUM_COLS used: 4.
     *
     * @param tiles     tiles for board
     * @param numRows number of rows of the board
     * @param numCols number of columns of the board
     */
    public Board(List<Tile> tiles, int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        numRevealed = 0;
        Iterator<Tile> tileIterator = tiles.iterator();
        this.tiles = new Tile[this.numRows][this.numCols];
        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                this.tiles[row][col] = tileIterator.next();
            }
        }
    }

    public int getNumRevealed() {
        return numRevealed;
    }

    public void addNumRevealed() {
        numRevealed = numRevealed++;
    }

    /**
     * return boardSize
     *
     * @return boardSize
     */
    int getBoardSize() {
        return numRows * numCols;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Return a string representation of this Board
     *
     * @return A string representation of the Tiles within this Board
     */
    @Override
    public String toString() {
        return "Board{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * Return an iterator of this Board
     *
     * @return A BoardIterator which contains all Tiles within this board, in row-major order.
     */
    @Override
    @NonNull
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    /**
     * The Iterator subclass returned by Board.iterator()
     */
    private class BoardIterator implements Iterator<Tile> {
        private int cursor;

        /**
         * An iterator containing the tiles of the Board in row-major order
         */
        BoardIterator() {
            this.cursor = 0;
        }

        /**
         * Tells if this iterator has a next element
         *
         * @return True iff this iterator has a next element to be returned when calling next()
         */
        @Override
        public boolean hasNext() {
            return this.cursor < (numRows * numCols);
        }

        /**
         * Gets the next Tile in the iterator
         *
         * @return The next Tile object, as long as there is one.
         */
        @Override
        public Tile next() {
            if (this.hasNext()) {
                int current = cursor;
                int row = current / numRows;
                int col = current % numCols;
                cursor++;
                return tiles[row][col];
            }
            throw new NoSuchElementException();
        }
    }
}
