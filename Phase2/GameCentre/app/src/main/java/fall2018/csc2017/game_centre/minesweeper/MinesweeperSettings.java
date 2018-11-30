package fall2018.csc2017.game_centre.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;

import fall2018.csc2017.game_centre.GameMenuActivity;
import fall2018.csc2017.game_centre.R;

public class MinesweeperSettings extends AppCompatActivity {

    /**
     * number of rows of the Minesweeper board.
     */
    private int numRows;
    private int numCols;
    private int numBombs;
//    private final int numCols = 10;

    /**
     * SlidingTiles Game in function.
     */
    private MinesweeperGame minesweeperGame;

    private EditText numBombsField;

    private String currentUsername;

    private String gameFilename;

    /**
     * Set up UI interface for SlidingTilesSettings.
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_minesweeper);

        currentUsername = getIntent().getStringExtra("USERNAME");
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");

        addStartButtonListener();
        setupBoardSizeSpinner();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupNumBombs();
                handler.postDelayed(this, 100);
            }
        }, 100);
        //setupNumBombs();
    }

    /**
     * Add drop-down menu to let user choose board size.
     * Code adapted from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setupBoardSizeSpinner() {
        Spinner boardSize;
        ArrayAdapter<CharSequence> adapter;

        boardSize = findViewById(R.id.msBoardSizeSpinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.MSGameSize,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boardSize.setAdapter(adapter);
        boardSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    numRows = 10;
                    numCols = 10;
                } else if (i == 1) {
                    numRows = 13;
                    numCols = 13;
                } else if (i == 2) {
                    numRows = 15;
                    numCols = 15;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setupNumBombs() {
        numBombsField = findViewById(R.id.msNumberBombs);
        if (!(numBombsField.getText().toString().equals(""))) {
            numBombs = Integer.valueOf(numBombsField.getText().toString());
        }
    }

    /**
     * Add start game button listener
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperGame = new MinesweeperGame(numRows, numCols, numBombs);
                System.out.println(numBombs);
                switchToGame();
                finish();
            }
        });
    }

    /**
     * Initiates a new game and switches the activity.
     */
    private void switchToGame() {
        if (TextUtils.isEmpty(numBombsField.getText().toString())) {
            Toast.makeText(MinesweeperSettings.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        } else if (invalidUserInput()) {
            Toast.makeText(MinesweeperSettings.this, "Invalid number of bombs", Toast.LENGTH_SHORT).show();
        } else {
            saveToFile(gameFilename);
            Intent gameIntent = new Intent(this, MinesweeperGameActivity.class);
            gameIntent.putExtra("USERNAME", currentUsername);
            gameIntent.putExtra("GAME_FILENAME", gameFilename);
            startActivity(gameIntent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        }
    }

    private boolean invalidUserInput() {
        return numBombs >= numRows * numCols;
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
            outputStream.writeObject(minesweeperGame);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void switchToGMA() {
        Intent msGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new MinesweeperGame(0, 0, 0));
        gmaBundle.putString("GAME_DESC", MinesweeperGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "MINESWEEPER");
        msGMAIntent.putExtras(gmaBundle);
        startActivity(msGMAIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        switchToGMA();
    }
}
