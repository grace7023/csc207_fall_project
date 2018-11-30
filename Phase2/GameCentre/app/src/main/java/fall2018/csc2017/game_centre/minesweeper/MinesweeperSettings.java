package fall2018.csc2017.game_centre.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;

import fall2018.csc2017.game_centre.GameMenuActivity;
import fall2018.csc2017.game_centre.R;

public class MinesweeperSettings extends AppCompatActivity {

    /**
     * the number of rows to be inputted into MinesweeperGame.
     */
    private int numRows;

    /**
     * the number of columns to be inputted into MinesweeperGame.
     */
    private int numCols;

    /**
     * the number of bombs to be inputted into MinesweeperGame.
     */
    private int numBombs;

    /**
     * the MinesweeperGame object that will be instantiated with the user inputted settings
     */
    private MinesweeperGame minesweeperGame;

    /**
     * the text space for the user to input the number of bombs
     */
    private EditText numBombsField;

    /**
     * the current logged in user's username as a String
     */
    private String currentUsername;

    /**
     * the file name for saving and loading
     */
    private String gameFilename;

    private Boolean DarkView;

    /**
     * Set up UI interface for MinesweeperSettings.
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_minesweeper);

        currentUsername = getIntent().getStringExtra("USERNAME");
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        DarkView = getIntent().getBooleanExtra("DARKVIEW", false);

        setUpDarkView();

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
    }

    /**
     * Add drop-down menu to let user choose board size.
     * Code adapted from https://stackoverflow.com/questions/13377361/how-to-create-a-drop-down-list
     */
    private void setupBoardSizeSpinner() {
        Spinner boardSize;
        ArrayAdapter<CharSequence> adapter;

        boardSize = findViewById(R.id.msBoardSizeSpinner);
        if (DarkView)
            adapter = ArrayAdapter.createFromResource(this, R.array.MSGameSize,
                    R.layout.white_spinner_item);
        else
            adapter = ArrayAdapter.createFromResource(this, R.array.MSGameSize,
                                                    android.R.layout.simple_spinner_dropdown_item);
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
    private void setUpDarkView(){
        if (DarkView){
            ConstraintLayout constraintLayout = findViewById(R.id.MSsettingsActivity);
            constraintLayout.setBackgroundColor(Color.DKGRAY);
            TextView gameSettings = findViewById(R.id.msSettingsHeading);
            gameSettings.setTextColor(Color.WHITE);
            TextView numBombsText = findViewById(R.id.bombNumberText);
            numBombsText.setTextColor(Color.WHITE);
            TextView boardSizeText = findViewById(R.id.boardSizeText);
            boardSizeText.setTextColor(Color.WHITE);
            EditText msNumberBombs = findViewById(R.id.msNumberBombs);
            msNumberBombs.setTextColor(Color.WHITE);
        }
    }
    /**
     * Gets the input for the number of bombs from the player
     */
    private void setupNumBombs() {
        numBombsField = findViewById(R.id.msNumberBombs);
        if (!(numBombsField.getText().toString().equals(""))) {
            numBombs = Integer.valueOf(numBombsField.getText().toString());
        }
    }

    /**
     * Adds the start game button with onclick to instantiate the MinesweeperGame with the previously inputted values
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperGame = new MinesweeperGame(numRows, numCols, numBombs);
                switchToGame();
            }
        });
    }

    /**
     * Initiates a new game and switches to the game activity.
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
            gameIntent.putExtra("FROM_LOAD", false);
            gameIntent.putExtra("DARKVIEW", DarkView);
            startActivity(gameIntent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            finish();
        }
    }

    /**
     * Returns true if the user inputted number of bombs is more than the number of tiles on the selected board
     *
     * @return true if inputted number of bombs is invalid, false if valid
     */
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

    /**
     * Switch to GameMenuActivity for Minesweeper. Pass the game, game description, game file name,
     * current username, and game name into the intent.
     */
    private void switchToGMA() {
        Intent msGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new MinesweeperGame(0, 0, 0));
        gmaBundle.putString("GAME_DESC", MinesweeperGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "MINESWEEPER");
        gmaBundle.putBoolean("DARKVIEW", DarkView);
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
