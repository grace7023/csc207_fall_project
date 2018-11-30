package fall2018.csc2017.game_centre.twenty_forty;

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

import fall2018.csc2017.game_centre.GameMenuActivity;
import fall2018.csc2017.game_centre.R;

public class TFSettings extends AppCompatActivity {

    /**
     * Board size.
     */
    private int gameSize;

    /**
     * TF Game in function.
     */
    private TFGame tfGame;

    /**
     * Name of current user
     */
    private String currentUsername;

    /**
     * Name of fileSave
     */
    private String gameFilename;

    /**
     * Whether this activity is in darkView mode or not.
     */

    private boolean darkView;

    /**
     * Set up UI interface for TFSettings.
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_slidingtiles);

        currentUsername = getIntent().getStringExtra("USERNAME");
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        darkView = getIntent().getBooleanExtra("DARKVIEW", false);

        addStartButtonListener();
        setupSpinner();
        setUpDarkView();
    }

    /**
     * Add drop-down menu to let user choose board size.
     * Code adapted from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setupSpinner() {
        Spinner boardSize;
        ArrayAdapter<CharSequence> adapter;

        boardSize = findViewById(R.id.stBoardSizeSpinner);
        if (darkView)
            adapter = ArrayAdapter.createFromResource(this, R.array.STGameSize,
                    R.layout.white_spinner_item);
        else
            adapter = ArrayAdapter.createFromResource(this, R.array.STGameSize,
                    android.R.layout.simple_spinner_dropdown_item);
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
                tfGame = new TFGame(gameSize);
                switchToGame();
                finish();
            }
        });
    }

    /**
     * Initiates a new game and switches the activity.
     */
    private void switchToGame() {
        saveToFile(gameFilename);
        Intent gameIntent = new Intent(this, TFGameActivity.class);
        gameIntent.putExtra("USERNAME", currentUsername);
        gameIntent.putExtra("GAME_FILENAME", gameFilename);
        gameIntent.putExtra("DARKVIEW", darkView);
        startActivity(gameIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
            outputStream.writeObject(tfGame);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Switch to GameMenuActivity for Minesweeper. Pass the game, game description, game file name,
     * current username, and game name into the intent.
     */
    private void switchToGMA() {
        Intent tfGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new TFGame(0));
        gmaBundle.putString("GAME_DESC", TFGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "2048");
        gmaBundle.putBoolean("DARKVIEW", darkView);
        tfGMAIntent.putExtras(gmaBundle);
        startActivity(tfGMAIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    /**
     * If onBack button is pressed then switch to GameMenuActivity
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
