package fall2018.csc2017.game_centre.sliding_tiles;

import fall2018.csc2017.game_centre.*;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SlidingTilesSettings extends AppCompatActivity {

    /**
     * Board size.
     */
    private int gameSize;

    /**
     * Whether this activity is in darkView mode or not.
     */

    private boolean darkView;

    /**
     * SlidingTiles Game in function.
     */
    private SlidingTilesGame slidingTilesGame;

    /**
     * the current user's username
     */

    private String currentUsername;

    /**
     * save file path for game
     */

    private String gameFilename;

    /**
     * Set up UI interface for SlidingTilesSettings.
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUsername = getIntent().getStringExtra("USERNAME");
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        darkView = getIntent().getBooleanExtra("DARKVIEW", false);

        setContentView(R.layout.settings_slidingtiles);
        addStartButtonListener();
        setupBoardSizeSpinner();

        setUpDarkView();
    }

    /**
     * Add drop-down menu to let user choose board size.
     * Code adapted from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setupBoardSizeSpinner() {
        Spinner boardSize;
        ArrayAdapter<CharSequence> adapter;

        boardSize = findViewById(R.id.stBoardSizeSpinner);
        if (darkView)
            adapter = ArrayAdapter.createFromResource(this, R.array.STGameSize,
                    R.layout.white_spinner_item);
        else
            adapter = ArrayAdapter.createFromResource(this, R.array.STGameSize,
                    android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boardSize.setAdapter(adapter);
        boardSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gameSize = Integer.valueOf(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Add start game button listener
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingTilesGame = new SlidingTilesGame(gameSize);
                switchToGame();
            }
        });
    }

    /**
     * Initiates a new game and switches the activity.
     */
    private void switchToGame() {
        saveToFile(gameFilename);
        Intent gameIntent = new Intent(this, SlidingTilesGameActivity.class);
        gameIntent.putExtra("USERNAME", currentUsername);
        gameIntent.putExtra("GAME_FILENAME", gameFilename);
        gameIntent.putExtra("DARKVIEW", darkView);
        startActivity(gameIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    /**
     * Switch to GameMenuActivity for Minesweeper. Pass the game, game description, game file name,
     * current username, and game name into the intent.
     */
    private void switchToGMA() {
        Intent gmaIntent = new Intent(this, GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new SlidingTilesGame(0));
        gmaBundle.putString("GAME_DESC", SlidingTilesGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "SLIDING TILES");
        gmaBundle.putBoolean("DARKVIEW", darkView);
        gmaIntent.putExtras(gmaBundle);
        startActivity(gmaIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    /**
     * Saves game to file.
     *
     * @param fileName path to save file
     */

    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(slidingTilesGame);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * When the back button is pressed, switch to the GameMenuActivity
     */
    @Override
    public void onBackPressed() {
        switchToGMA();
    }

    /**
     * Setting up the activity to be in darkView mode.
     */

    private void setUpDarkView() {
        if (darkView) {
            ConstraintLayout constraintLayout = findViewById(R.id.slidingTilesSettingsActivity);
            constraintLayout.setBackgroundColor(Color.DKGRAY);
            TextView settingsHeading = findViewById(R.id.SlidingTilesSettingsHeading);
            settingsHeading.setTextColor(Color.WHITE);
            TextView stSpinner = findViewById(R.id.stBoardSpinnerText);
            stSpinner.setTextColor(Color.WHITE);

        }
    }
}
