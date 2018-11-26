package fall2018.csc2017.game_centre;

import android.util.Log;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * The functionality of the scoreboard for SlidingTiles
 */
public class Scoreboard implements Serializable {

    /**
     * The scores of the game
     */
    private ArrayList<Score> scores;

    /**
     * The directory to save the file to
     */
    private static String dir = "data/data/fall2018.csc2017.slidingtiles/files/";

    /**
     * The name of the file being saved
     */
    private static File scoreFile = new File(dir + "SlidingTiles.ser");


    public Scoreboard() {
        scores = new ArrayList<>();
    }

    /**
     * Adds the player's new score to the current list of scores
     *
     * @param player the current player
     * @param score the score they just achieved
     */
    public void addScore(String player, String score) {
        scores.add(new Score(player, score));
    }

    /**
     * returns all the scores for the game
     * @return scores for the game
     */
    ArrayList<Score> getScores() {
        Collections.sort(scores);
        return scores;
    }

    /**
     * returns the scores specific to the player for the game
     *
     * @param player the current player
     * @return the player's scores
     */
    ArrayList<Score> getScores(String player) {
        ArrayList<Score> output = new ArrayList<>();
        for (Score score : scores) {
            if (score.player.equals(player)) {
                output.add(score);
            }
        }
        Collections.sort(output);
        return output;
    }

    /**
     * Loads the scoreboard from the file
     * @return the scoreboard for the game
     */
    public static Scoreboard loadFromFile() {
        Scoreboard file = new Scoreboard();
        try {
            InputStream inputStream = new FileInputStream(scoreFile);
            ObjectInputStream input = new ObjectInputStream(inputStream);
            file = (Scoreboard) input.readObject();
            input.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        return file;
    }

    /**
     * Saves the scoreboard to the file
     */
    public void saveToFile() {
        try {
            FileOutputStream outputStream = new FileOutputStream(scoreFile);
            ObjectOutputStream objOut = new ObjectOutputStream(outputStream);
            objOut.writeObject(this);
            objOut.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("login activity", "Can not find file: " + e.toString());
        }
    }
}
