package fall2018.csc2017.game_centre.twenty_forty;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.io.Serializable;

public class TFBoard extends Observable implements Serializable, Iterable<Box> {

    private int boardSize;
    private Box[][] boxes;

    public TFBoard (List<Box> boxesList, int boardSize) {

        this.boardSize = boardSize;
        Iterator<Box> boxIterator = boxesList.iterator();
        this.boxes = new Box[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                this.boxes[row][col] = boxIterator.next();
            }
        }
    }

    public Box getBox(int row, int col) {
        return this.boxes[row][col];
    }

    /**
     * Combines the Box at (row1, col1) with the Box at (row2, col2) in boxes
     * @param row1 the row of the Box which stays in place
     * @param col1 the col of the Box which stays in place
     * @param row2 the row of the Box which moves into the position of the other
     * @param col2 the row of the Box which moves into the position of the other
     */
    private void combineTiles (int row1, int col1, int row2, int col2) {
        int newExponent = boxes[row1][col1].getExponent() + 1;
        boxes[row1][col1].setExponent(newExponent);
        boxes[row2][col2].setExponent(0);
    }

    @NonNull
    @Override
    public Iterator<Box> iterator() {
        return new TFBoardIterator();
    }

    @Override
    public String toString() {
        return "TFBoard{" +
                "boxes=" + Arrays.toString(boxes) +
                '}';
    }

    private int getBoardSize() {
        return this.boardSize * this.boardSize;
    }

    private class TFBoardIterator implements Iterator<Box> {

        private int cursor;

        TFBoardIterator() { this.cursor = 0;}

        @Override
        public boolean hasNext() {
            return (cursor < getBoardSize());
        }

        @Override
        public Box next() {
            if (this.hasNext()) {
                int current = cursor;
                int row = current / boardSize;
                int col = current % boardSize;
                cursor++;
                return boxes[row][col];
            }
            throw new NoSuchElementException();
        }
    }
}
