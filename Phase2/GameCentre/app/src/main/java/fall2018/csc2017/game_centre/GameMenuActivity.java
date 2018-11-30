package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameMenuActivity extends AppCompatActivity {

    /**
     * File path for saving game
     */
    private String gameFilename;

    /**
     * Name of current user
     */
    private String currentUsername;

    /**
     * This menu's game
     */
    private Game game;

    /**
     * Game description
     */
    private String gameDesc;

    /**
     * Boolean value corresponding to whether the game is in dark mode or not
     */
    private boolean darkView;

    /**
     * Create UI for a game menu
     *
     * @param savedInstanceState a bundle
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        Bundle gameBundle = getIntent().getExtras();
        assert gameBundle != null;
        game = (Game) gameBundle.getSerializable("GAME");
        currentUsername = gameBundle.getString("USERNAME");
        gameFilename = gameBundle.getString("GAME_FILENAME");
        gameDesc = gameBundle.getString("GAME_DESC");
        TextView textView = findViewById(R.id.GameText);
        textView.setText(gameDesc);


        addStartButtonListener();
        addLoadButtonListener();
        addReturnButtonListener();
        addScoreboardButtonListener();


        darkView = gameBundle.getBoolean("DARKVIEW");
        setDarkView();
    }

    /**
     * Puts the app into dark mode by setting the background colour
     * to dark grey and the text colour to white
     */
    private void setDarkView() {
        if (darkView) {
            RelativeLayout relativeLayout = findViewById(R.id.gameMenuActivity);
            relativeLayout.setBackgroundColor(Color.DKGRAY);
            TextView gameText = findViewById(R.id.GameText);
            gameText.setTextColor(Color.WHITE);
        }
    }

    /**
     * Add a start game button.
     */

    protected void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSettings();
            }
        });
    }

    /**
     * Add a load game button
     */

    protected void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getScreenSize() != 0 && !game.isOver()) {
                    loadFromFile(gameFilename);
                    saveToFile(gameFilename);
                    makeToastLoadedText();
                    switchToGame();
                } else {
                    Toast.makeText(GameMenuActivity.this, "There is no save file!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    /**
     * Add a return to game launcher button
     */

    protected void addReturnButtonListener() {
        Button returnButton = findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(gameFilename);
                switchToMainMenu();
            }
        });
    }

    /**
     * Add a scoreboard button
     */

    protected void addScoreboardButtonListener() {
        Button scoreButton = findViewById(R.id.scoreButton);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(gameFilename);
                switchToScoreboard();
            }
        });
    }

    /**
     * Tells user that a game was loaded.
     */

    protected void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Resume a game that was saved.
     */

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(gameFilename);
    }

    /**
     * Switch current screen to game activity.
     */

    protected void switchToGame() {
        saveToFile(gameFilename);
        Intent gameIntent = game.getGameActivityIntent(this);
        gameIntent.putExtra("USERNAME", currentUsername);
        gameIntent.putExtra("GAME_FILENAME", gameFilename);
        gameIntent.putExtra("FROM_LOAD", true);
        gameIntent.putExtra("DARKVIEW", darkView);
        startActivity(gameIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    /**
     * Switch current screen to a game settings screen
     */

    protected void switchToSettings() {
        saveToFile(gameFilename);
        Intent settingsIntent = game.getSettingsIntent(this);
        settingsIntent.putExtra("USERNAME", currentUsername);
        settingsIntent.putExtra("GAME_FILENAME", gameFilename);
        settingsIntent.putExtra("DARKVIEW", darkView);
        startActivity(settingsIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    /**
     * Switch to game launcher main menu.
     */

    protected void switchToMainMenu() {
        saveToFile(gameFilename);
        Intent mainMenuIntent = new Intent(this, GameManager.class);
        mainMenuIntent.putExtra("USERNAME", currentUsername);
        mainMenuIntent.putExtra("DARKVIEW", darkView);
        startActivity(mainMenuIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    /**
     * Switch to scoreboard
     */

    protected void switchToScoreboard() {
        Intent scoreboardIntent = new Intent(getApplicationContext(), ScoreboardActivity.class);
        Bundle scoreboardBundle = new Bundle();
        scoreboardBundle.putSerializable("GAME", game);
        scoreboardBundle.putString("GAME_DESC", gameDesc);
        scoreboardBundle.putString("GAME_FILENAME", gameFilename);
        scoreboardBundle.putString("USERNAME", currentUsername);
        assert getIntent().getExtras() != null;
        scoreboardBundle.putString("GAME_NAME", getIntent().getExtras().getString("GAME_NAME"));
        scoreboardBundle.putBoolean("DARKVIEW", darkView);
        scoreboardIntent.putExtras(scoreboardBundle);
        startActivity(scoreboardIntent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        finish();
    }

    /**
     * Load saved game from file
     * Adapted from: https://www.journaldev.com/927/objectoutputstream-java-write-object-file
     *
     * @param filename file path
     */

    protected void loadFromFile(String filename) {
        try {
            InputStream inputStream = this.openFileInput(filename);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                game = (Game) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save current game to file.
     * Adapted from: https://www.journaldev.com/927/objectoutputstream-java-write-object-file
     *
     * @param fileName file path
     */

    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(game);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        switchToMainMenu();
    }
}
