package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * A Game abstract class.
 */

public abstract class Game {

    /**
     * Return whether a tap at position is valid
     *
     * @param position what position the tap is
     * @return whether the tap is valid
     */
    public abstract boolean isValidTap(int position);

    /**
     * Perform a move based on the tap at position.
     *
     * @param position where the tap occurred
     */
    public abstract void touchMove(int position);

    /**
     * Return whether the Game is over.
     *
     * @return whether Game is over
     */
    public abstract boolean isOver();

    /**
     * Undo function that reverses the most recent move.
     */
    public abstract void undo();

    /**
     * Size of the relevant screen real estate for game.
     */
    public abstract int getScreenSize();

    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        throw new UnsupportedOperationException("Stub!");
    }

    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        throw new UnsupportedOperationException("Stub!");
    }
}
