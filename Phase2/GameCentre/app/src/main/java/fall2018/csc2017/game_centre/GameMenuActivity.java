package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private String gameFileName;

    private String currentUsername;

    /**
     * Thus menu's game
     */
    private Game game;

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
        gameFileName = gameBundle.getString("GAME_FILENAME");

        TextView textView = findViewById(R.id.GameText);
        textView.setText(gameBundle.getString("GAME_DESC"));

        addStartButtonListener();
        addLoadButtonListener();
        addReturnButtonListener();
        addScoreboardButtonListener();
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
                    loadFromFile(gameFileName);
                    saveToFile(gameFileName);
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
                saveToFile(gameFileName);
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
                saveToFile(gameFileName);
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
        loadFromFile(gameFileName);
    }

    /**
     * Switch current screen to game activity.
     */

    protected void switchToGame() {
        saveToFile(gameFileName);
        Intent gameIntent = game.getGameActivityIntent(this);
        gameIntent.putExtra("USERNAME", currentUsername);
        gameIntent.putExtra("GAME_FILENAME", gameFileName);
        startActivity(gameIntent);
    }

    /**
     * Switch current screen to a game settings screen
     */

    protected void switchToSettings() {
        saveToFile(gameFileName);
        Intent settingsIntent = game.getSettingsIntent(this);
        settingsIntent.putExtra("USERNAME", currentUsername);
        settingsIntent.putExtra("GAME_FILENAME", gameFileName);
        startActivity(settingsIntent);
    }

    /**
     * Switch to game launcher main menu.
     */

    protected void switchToMainMenu() {
        saveToFile(gameFileName);
        Intent mainMenuIntent = new Intent(this, GameManager.class);
        mainMenuIntent.putExtra("USERNAME", currentUsername);
        startActivity(mainMenuIntent);
    }

    /**
     * Switch to scoreboard
     */

    protected void switchToScoreboard() {
        Intent scoreboardIntent = new Intent(this, ScoreboardActivity.class);
        scoreboardIntent.putExtra("USERNAME", currentUsername);
        startActivity(scoreboardIntent);
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
}
