package fall2018.csc2017.game_centre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * GameActivity abstract class
 */
public abstract class GameActivity extends AppCompatActivity {

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public abstract void display();

    /**
     * Set up UI interface for Game
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    public void onPause() {
        super.onPause();
    }

    /**
     * Load the Game board manager from fileName.
     *
     * @param fileName the name of the file
     */
    public abstract void loadFromFile(String fileName);

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public abstract void saveToFile(String fileName);

    /**
     * Auto save function that saves Game after each move.
     */
    public abstract void autoSave();
}
