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

    TFBoard (List<Box> boxesList, int boardSize) {

        this.boardSize = boardSize;
        Iterator<Box> boxIterator = boxesList.iterator();
        this.boxes = new Box[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                this.boxes[row][col] = boxIterator.next();
            }
        }
    }

    Box getBox(int row, int col) {
        return this.boxes[row][col];
    }

    /**
     * Combines the Box at (row1, col1) with the Box at (row2, col2) in boxes
     * @param row1 the row of the Box which stays in place
     * @param col1 the col of the Box which stays in place
     * @param row2 the row of the Box which moves into the position of the other
     * @param col2 the row of the Box which moves into the position of the other
     */
    private void combineBoxes (int row1, int col1, int row2, int col2) {
        int newExponent = boxes[row1][col1].getExponent() + 1;
        boxes[row1][col1].setExponent(newExponent);
        boxes[row2][col2].setExponent(0);
    }

    private void swapBoxes (int row1, int col1, int row2, int col2) {
        Box temp = boxes[row1][col1];
        boxes[row1][col1] = boxes[row2][col2];
        boxes[row2][col2] = temp;
    }

    void moveBoxesUp() {
        // Skips the top row since it can't go up any further
        for (int row = 1; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int curRow = row;
                while (curRow > 0) {
                    if (boxes[curRow - 1][col].getExponent() == 0) {
                        // There's space for this box to freely move
                        swapBoxes(curRow, col, curRow - 1, col);
                        curRow -= 1;
                    }
                    else {
                        // This box has another in its way!
                        if (boxes[curRow][col].getExponent() ==
                                boxes[curRow - 1][col].getExponent()) {
                            combineBoxes(curRow - 1, col, curRow, col);
                        }
                        break;
                    }
                }
            }
        }
    }

    void moveBoxesDown() {
        // Skips the bottom row since it can't go down any further
        for (int row = boardSize - 2; 0 <= row; row--) {
            for (int col = 0; col < boardSize; col++) {
                int curRow = row;
                while (boardSize + 1 < curRow) {
                    if (boxes[curRow + 1][col].getExponent() == 0) {
                        // There's space for this box to freely move
                        swapBoxes(curRow, col, curRow + 1, col);
                        curRow -= 1;
                    }
                    else {
                        // This box has another in its way!
                        if (boxes[curRow][col].getExponent() ==
                                boxes[curRow + 1][col].getExponent()) {
                            combineBoxes(curRow + 1, col, curRow, col);
                        }
                        break;
                    }
                }
            }
        }
    }

    void moveBoxesLeft() {
        // Skips the left column since it can't go left any further
        for (int col = 1; col < boardSize; col++) {
            for (int row = 0; row < boardSize; row++) {
                int curCol = col;
                while (0 < curCol) {
                    if (boxes[row][curCol - 1].getExponent() == 0) {
                        // There's space for this box to freely move
                        swapBoxes(row, curCol, row, curCol - 1);
                        curCol -= 1;
                    }
                    else {
                        // This box has another in its way!
                        if (boxes[row][curCol].getExponent() ==
                                boxes[row][curCol - 1].getExponent()) {
                            combineBoxes(row, curCol - 1, row, curCol);
                        }
                        break;
                    }
                }
            }
        }
    }

    void moveBoxesRight() {
        // Skips the left column since it can't go left any further
        for (int col = boardSize - 2; 0 <= col; col--) {
            for (int row = 0; row < boardSize; row++) {
                int curCol = col;
                while (curCol < boardSize - 1) {
                    if (boxes[row][curCol + 1].getExponent() == 0) {
                        // There's space for this box to freely move
                        swapBoxes(row, curCol, row, curCol + 1);
                        curCol += 1;
                    }
                    else {
                        // This box has another in its way!
                        if (boxes[row][curCol].getExponent() ==
                                boxes[row][curCol + 1].getExponent()) {
                            combineBoxes(row, curCol + 1, row, curCol);
                        }
                        break;
                    }
                }
            }
        }
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

    int getBoardSize() {
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
