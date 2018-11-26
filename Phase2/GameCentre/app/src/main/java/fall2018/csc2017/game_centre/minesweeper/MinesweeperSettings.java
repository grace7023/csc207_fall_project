package fall2018.csc2017.game_centre.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.io.ObjectOutputStream;

import fall2018.csc2017.game_centre.GameMenuActivity;
import fall2018.csc2017.game_centre.R;

public class MinesweeperSettings extends AppCompatActivity {

    /**
     * Board size.
     */
    private int gameSize;

    /**
     * SlidingTiles Game in function.
     */
    private MinesweeperGame minesweeperGame;

    /**
     * Set up UI interface for SlidingTilesSettings.
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_minesweeper);
        addStartButtonListener();
        setupSpinner();
    }

    /**
     * Add drop-down menu to let user choose board size.
     * Code adapted from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setupSpinner() {
        EditText boardSize;
        ArrayAdapter<CharSequence> adapter;

    }

    /**
     * Add start game button listener
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperGame = new MinesweeperGame(gameSize, gameSize, 10); //TODO: change this
                switchToGame();
                finish();
            }
        });
    }

    /**
     * Initiates a new game and switches the activity.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, MinesweeperGameActivity.class);
        saveToFile(GameMenuActivity.filename);
        startActivity(tmp);
    }

    /**
     * Saves game to file.
     * @param fileName path to save file
     */

    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(minesweeperGame);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
