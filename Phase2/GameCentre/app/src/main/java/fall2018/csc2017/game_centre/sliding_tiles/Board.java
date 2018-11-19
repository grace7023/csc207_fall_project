package fall2018.csc2017.game_centre.sliding_tiles;

import android.support.annotation.NonNull;

import java.util.NoSuchElementException;
import java.util.Observable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * size of the board.
     */
    private int boardSize;

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == boardSize * boardSize
     * default values for NUM_ROWS and NUM_COLS used: 4.
     *
     * @param tiles     tiles for board
     * @param boardSize size of the board
     */
    public Board(List<Tile> tiles, int boardSize) {
        this.boardSize = boardSize;
        Iterator<Tile> tileIterator = tiles.iterator();
        this.tiles = new Tile[boardSize][boardSize];
        for (int row = 0; row != boardSize; row++) {
            for (int col = 0; col != boardSize; col++) {
                this.tiles[row][col] = tileIterator.next();
            }
        }
    }

    /**
     * return boardSize
     *
     * @return boardSize
     */
    int getBoardSize() {
        return boardSize;
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
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    public void swapTiles(int row1, int col1, int row2, int col2) {

        Tile temp = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = temp;

        setChanged();
        notifyObservers();
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
            return this.cursor < (boardSize * boardSize);
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
                int row = current / boardSize;
                int col = current % boardSize;
                cursor++;
                return tiles[row][col];
            }
            throw new NoSuchElementException();
        }
    }
}
