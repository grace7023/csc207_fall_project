package fall2018.csc2017.game_centre.twenty_forty;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.Observable;
import java.io.Serializable;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TFBoard extends Observable implements Serializable, Iterable<Box> {

    @NonNull
    @Override
    public Iterator<Box> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Box> action) {

    }

    @Override
    public Spliterator<Box> spliterator() {
        return null;
    }
}
