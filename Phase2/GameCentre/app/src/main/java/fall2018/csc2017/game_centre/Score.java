package fall2018.csc2017.game_centre;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * A score to be shown in the scoreboard
 */
public class Score implements Comparable<Score>, Serializable {

    /**
     * The numerical value of the score
     */
    private String value;

    /**
     * The player who achieved this score
     */
    private String player;

    /**
     * Create a new score with player and value.
     *
     * @param player player
     * @param value  value of score
     */

    Score(String player, String value) {
        this.player = player;
        this.value = value;
    }

    /**
     * Returns the player who made this score
     *
     * @return the name of the player
     */
    String getPlayer() {
        return player;
    }

    /**
     * Allows for the scores to be comparable by their numerical value
     *
     * @param o the score being compared to
     */
    @Override
    public int compareTo(@NonNull Score o) {
        if (Pattern.matches("\\d+", this.value) && Pattern.matches("\\d+", o.value)) {
            return Integer.valueOf(this.value).compareTo(Integer.valueOf(o.value));
        }
        return this.value.compareTo(o.value);
    }

    /**
     * Checks if other has the same value as self
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Score) {
            Score o = (Score) other;
            return value.equals(o.value);
        }
        return false;
    }

    /**
     * Returns the string representation of a score
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return player + ": " + value;
    }
}
