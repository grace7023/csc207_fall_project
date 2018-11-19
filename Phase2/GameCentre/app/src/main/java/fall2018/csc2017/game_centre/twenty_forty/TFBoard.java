package fall2018.csc2017.game_centre.twenty_forty;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.io.Serializable;
import java.util.Spliterator;
import java.util.function.Consumer;

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

    @NonNull
    @Override
    public Iterator<Box> iterator() {
        return new TFBoardIterator();
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
