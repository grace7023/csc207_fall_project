package fall2018.csc2017.game_centre.sliding_tiles;

import fall2018.csc2017.game_centre.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class SlidingTilesSettings extends AppCompatActivity {

    /**
     * Board size.
     */
    private int gameSize;

    /**
     * SlidingTiles Game in function.
     */
    private SlidingTilesGame slidingTilesGame;

    private String currentUsername;

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

        setContentView(R.layout.settings_slidingtiles);
        addStartButtonListener();
        setupBoardSizeSpinner();
    }

    /**
     * Add drop-down menu to let user choose board size.
     * Code adapted from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setupBoardSizeSpinner() {
        Spinner boardSize;
        ArrayAdapter<CharSequence> adapter;

        boardSize = findViewById(R.id.stBoardSizeSpinner);
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
                finish();
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
        startActivity(gameIntent);
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
}
