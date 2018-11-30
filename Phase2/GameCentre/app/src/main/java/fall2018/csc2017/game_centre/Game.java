package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import java.io.Serializable;

/**
 * A Game abstract class.
 */

public abstract class Game implements Serializable {

    /**
     * Return whether a move with parameter arg is valid
     *
     * @param arg what argument representing the move attempted
     * @return whether the tap is valid
     */
    public abstract boolean isValidMove(int arg);

    /**
     * Perform a move identified by arg.
     *
     * @param arg integer identifying the move
     */
    public abstract void move(int arg);

    /**
     * Return whether the Game is over.
     *
     * @return whether Game is over
     */
    public abstract boolean isOver();

    /**
     * Size of the relevant screen real estate for game.
     */
    public abstract int getScreenSize();

    /**
     * returns game over text depends on the outcome of the game
     * @return Win or lose
     */
    public abstract String gameOverText();

    /**
     * Changes activity from game to settings
     *
     * @param PackageContext game activity context
     * @return SlidingTilesSettings a
     */
    public Intent getSettingsIntent(AppCompatActivity PackageContext) {
        throw new UnsupportedOperationException("Stub!");
    }

    /**
     * Return GameActivity intent
     * @param PackageContext game activity context
     * @return Intent of Game Activity
     */
    public Intent getGameActivityIntent(AppCompatActivity PackageContext) {
        throw new UnsupportedOperationException("Stub!");
    }
}
