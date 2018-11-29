package fall2018.csc2017.game_centre;

import android.support.annotation.NonNull;
import java.io.Serializable;

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
     * @param player player
     * @param value value of score
     */

     Score(String player, String value) {
        this.player = player;
        this.value = value;
    }

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
//        return Integer.compare(this.value, o.value);
        return this.value.compareTo(o.value);
//        Integer.valueOf(numStr)
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
