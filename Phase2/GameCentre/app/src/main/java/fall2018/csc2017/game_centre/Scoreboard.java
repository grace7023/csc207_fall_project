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
import java.util.Comparator;
import java.util.List;


/**
 * The functionality of the scoreboard for SlidingTiles
 */
public class Scoreboard implements Serializable {

    /**
     * The scores of the game
     */
    private List<Score> scores;

    /**
     * The directory to save the file to
     */
    private final String dir = "data/data/fall2018.csc2017.slidingtiles/files/";

    /**
     * The name of the file being saved, which is "[GameName]Score.ser"
     */
    private File scoreFile;

    /**
     * Creates a new scoreboard with a game name
     *
     * @param game the name of the game
     */
    public Scoreboard(String game) {
        scoreFile = new File(dir + game + "Score.ser");
        scores = new ArrayList<>();
    }

    /**
     * Adds the player's new score to the current list of scores
     *
     * @param player the current player
     * @param score  the score they just achieved
     */
    public void addScore(String player, String score) {
        scores.add(new Score(player, score));
    }

    /**
     * returns all the scores for the game
     *
     * @return scores for the game
     */
    List<Score> getScores() {
        return scores;
    }

    /**
     * returns the scores specific to the player for the game
     *
     * @param player the current player
     * @return the player's scores
     */
    List<Score> getScores(String player) {
        ArrayList<Score> output = new ArrayList<>();
        for (Score score : scores) {
            if (score.getPlayer().equals(player)) {
                output.add(score);
            }
        }
        return output;
    }

    /**
     * Sorts the scores in ascending order
     */
    public void sortAscending() {
        Collections.sort(scores);
    }

    /**
     * Sorts the scores in descending order
     */
    public void sortDescending() {
        scores.sort(Collections.<Score>reverseOrder());
    }

    /**
     * Loads the scoreboard from the file and stores its scores in this scoreboard
     */
    public void loadFromFile() {
        Scoreboard file = new Scoreboard("");
        try {
            InputStream inputStream = new FileInputStream(scoreFile);
            ObjectInputStream input = new ObjectInputStream(inputStream);
            file = (Scoreboard) input.readObject();
            input.close();
            inputStream.close();

        } catch (FileNotFoundException e) {
            saveToFile();
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        this.scores = file.getScores();
    }

    /**
     * Saves the scoreboard to the file named "[GameName]Score.ser"
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
