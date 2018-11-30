package fall2018.csc2017.game_centre.sliding_tiles;

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
public class SlidingTilesGameActivity extends GameActivity implements Observer {

    /**
     * The board manager for SlidingTiles.
     */
    private SlidingTilesGame slidingTilesGame;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView<SlidingTilesGame> gridView;
    private int columnWidth, columnHeight;

    /**
     * TextView for currentScore
     */
    private TextView currentScore;

    private String currentUsername;

    private String gameFilename;

    /**
     *
     */
    private boolean darkView;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * If the game is over, save the score and return to the menu
     */
    private void checkIsOver() {
        if (slidingTilesGame.isOver()) {
            Scoreboard scoreboard = new Scoreboard("SlidingTiles");
            scoreboard.loadFromFile();

            scoreboard.addScore(currentUsername, String.valueOf(slidingTilesGame.getScore()));
            scoreboard.sortAscending();
            scoreboard.saveToFile();

            switchToGameOver();
        }
    }

    /**
     * Switch to the GameOverActivity. Happens when the
     * game ends regardless of whether you won or not.
     */
    public void switchToGameOver() {
        Intent gameOverIntent = new Intent(getApplicationContext(), GameOverActivity.class);

        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", slidingTilesGame);
        gmaBundle.putString("GAME_DESC", SlidingTilesGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "SLIDING TILES");
        gmaBundle.putBoolean("DARKVIEW", darkView);
        gameOverIntent.putExtras(gmaBundle);
        startActivity(gameOverIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
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
        createTileButtons(this);
        setContentView(R.layout.activity_slidingtiles_game);
        addUndoButton();
        addCurrentScore();
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(slidingTilesGame.getScreenSize());
        gridView.setGame(slidingTilesGame);
        slidingTilesGame.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / slidingTilesGame.getScreenSize();
                        columnHeight = (displayHeight - 200) / slidingTilesGame.getScreenSize();

                        display();
                    }
                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        STBoard board = slidingTilesGame.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != slidingTilesGame.getScreenSize(); row++) {
            for (int col = 0; col != slidingTilesGame.getScreenSize(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        STBoard board = slidingTilesGame.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / slidingTilesGame.getScreenSize();
            int col = nextPos % slidingTilesGame.getScreenSize();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
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
                slidingTilesGame = (SlidingTilesGame) input.readObject();
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
            outputStream.writeObject(slidingTilesGame);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateScore();
        updateTileButtons();
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
                if (slidingTilesGame.canUndoMove()) {
                    slidingTilesGame.undo();
                } else {
                    Toast.makeText(SlidingTilesGameActivity.this, "No more undo's", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addCurrentScore() {
        currentScore = findViewById(R.id.currentScore);
    }

    private void updateScore() {
        String newScore = "Score: " + String.valueOf(slidingTilesGame.getScore());
        currentScore.setText(newScore);
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

    @Override
    public void onBackPressed() {
        switchToGMA();
    }
}
