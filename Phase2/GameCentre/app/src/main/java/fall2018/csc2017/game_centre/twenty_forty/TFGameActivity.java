package fall2018.csc2017.game_centre.twenty_forty;

import fall2018.csc2017.game_centre.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * The game activity for SlidingTiles.
 */
public class TFGameActivity extends GameActivity implements Observer {

    /**
     * The board manager for SlidingTiles.
     */
    private TFGame tfGame;


    /**
     * The buttons to display.
     */
    private ArrayList<Button> boxButtons;


    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView<TFGame> gridView;

    /**
     * Column width and column height
     */
    private int columnWidth, columnHeight;

    /**
     * Name of current user
     */
    private String currentUsername;

    /**
     * Name of current FileName
     */
    private String gameFilename;

    /**
     * TextView for currentScore
     */
    private TextView currentScore;

    /**
     * Whether this activity is in darkView mode or not.
     */

    private boolean darkView;


    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        gridView.setAdapter(new CustomAdapter(boxButtons, columnWidth, columnHeight));
    }

    /**
     * If the game is over, save the score and return to the menu
     */
    private void checkIsOver() {
        if (tfGame.isOver()) {
            Scoreboard scoreboard = new Scoreboard("TwentyForty");
            scoreboard.loadFromFile();
            scoreboard.addScore(currentUsername, String.valueOf(tfGame.getScore()));
            scoreboard.sortDescending();
            scoreboard.saveToFile();
            switchToGameover();
        }
    }

    /**
     * Set up UI interface for SlidingTilesGame.
     *
     * @param savedInstanceState a bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUsername = getIntent().getStringExtra("USERNAME");
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        darkView = getIntent().getBooleanExtra("DARKVIEW", false);
        loadFromFile(gameFilename);
        createBoxButtons(this);
        setContentView(R.layout.activity_slidingtiles_game);
        addUndoButton();
        addCurrentScore();
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(tfGame.getScreenSize());
        gridView.setGame(tfGame);
        tfGame.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / tfGame.getScreenSize();
                        columnHeight = (displayHeight - 200) / tfGame.getScreenSize();

                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createBoxButtons(Context context) {
        TFBoard board = tfGame.getBoard();
        boxButtons = new ArrayList<>();
        for (int row = 0; row != tfGame.getScreenSize(); row++) {
            for (int col = 0; col != tfGame.getScreenSize(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getBox(row, col).getBackground());
                this.boxButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateBoxButtons() {
        TFBoard board = tfGame.getBoard();
        int nextPos = 0;
        for (Button b : boxButtons) {
            int row = nextPos / tfGame.getScreenSize();
            int col = nextPos % tfGame.getScreenSize();
            b.setBackgroundResource(board.getBox(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    public void onPause() {
        super.onPause();
        autoSave();
    }

    /**
     * Load the SlidingTiles board manager from fileName.
     *
     * @param fileName the name of the file
     */
    public void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                tfGame = (TFGame) input.readObject();
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
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
     * updating the display and autosave the game
     *
     * @param o   Observable
     * @param arg Object
     */
    @Override
    public void update(Observable o, Object arg) {
        updateScore();
        updateBoxButtons();
        display();
        autoSave();
        checkIsOver();
    }

    /**
     * Auto save function that saves Game after each move.
     */
    public void autoSave() {
        saveToFile(gameFilename);
    }

    /**
     * Add undo button listener.
     */
    public void addUndoButton() {
        Button undoButton = findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tfGame.canUndoMove()) {
                    tfGame.undo();
                } else {
                    Toast.makeText(TFGameActivity.this, "No more undo's", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Add currentScore field
     */
    public void addCurrentScore() {
        currentScore = findViewById(R.id.currentScore);
    }

    /**
     * Update score from current game state
     */
    private void updateScore() {
        String newScore = "Score: " + String.valueOf(tfGame.getScore());
        currentScore.setText(newScore);
    }

    /**
     * Switch to GameOver Activity
     */
    private void switchToGameover() {
        Intent gameoverIntent = new Intent(getApplicationContext(), GameOverActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", tfGame);
        gmaBundle.putString("GAME_DESC", TFGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "2048");
        gmaBundle.putBoolean("DARKVIEW", darkView);
        gameoverIntent.putExtras(gmaBundle);
        startActivity(gameoverIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    /**
     * Switch to GameMenuActivity for Minesweeper. Pass the game, game description, game file name,
     * current username, and game name into the intent.
     */
    private void switchToGMA() {
        Intent tfGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new TFGame(4));
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
     * Listens if back button is pressed
     */
    @Override
    public void onBackPressed() {
        switchToGMA();
    }
}
