package fall2018.csc2017.game_centre.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.game_centre.CustomAdapter;
import fall2018.csc2017.game_centre.GameActivity;
import fall2018.csc2017.game_centre.GameMenuActivity;
import fall2018.csc2017.game_centre.GameOverActivity;
import fall2018.csc2017.game_centre.GestureDetectGridView;
import fall2018.csc2017.game_centre.R;
import fall2018.csc2017.game_centre.Scoreboard;

/**
 * The game activity for SlidingTiles.
 */
public class MinesweeperGameActivity extends GameActivity implements Observer {

    /**
     * The board manager for SlidingTiles.
     */
    private MinesweeperGame minesweeperGame;

    /**
     * The buttons to display.
     */
    private List<Button> tileButtons = new ArrayList<>();

    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView<MinesweeperGame> gridView;

    /**
     * the height and width of a column of the grid
     */
    private int columnWidth, columnHeight;

    /**
     * set to "Flagging" if user is flagging and "Revealing" if user is revealing
     */
    private TextView flagging;

    /**
     * Textview telling user whether they are flagging tiles or revealing tiles
     */
    String flagText;

    /**
     * Whether game is over
     */
    private boolean GameOver = false;


    private boolean DarkView;
    /**
     * TextView for currentScore
     */
    private TextView timer;

    /**
     * Textview that tells user number of bombs subtracting number of flags
     */
    private TextView bombCounter;

    /**
     * the username of the current user
     */
    private String currentUsername;

    /**
     * the name of the save file for a Minesweeper game
     */
    private String gameFilename;


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
        if (minesweeperGame.isOver()) {
            if (minesweeperGame.puzzleSolved()) {
                Scoreboard scoreboard = new Scoreboard("Minesweeper");
                scoreboard.loadFromFile();
                scoreboard.addScore(currentUsername, minesweeperGame.getTime());
                scoreboard.sortAscending();
                scoreboard.saveToFile();
            } else {
                if (!GameOver) {
                    revealAllBombs();
                }
            }
            switchToGameOver();
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
        DarkView = getIntent().getBooleanExtra("DARKVIEW", false);
        loadFromFile(gameFilename);
        minesweeperGame.setLoadTime();
        createTileButtons(this);
        setContentView(R.layout.activity_minesweeper_game);
        addFlagButton();
        addTimer();
        addBombCounter();
        toggleFlaggingText();
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(minesweeperGame.getNumCols());
        gridView.setGame(minesweeperGame);
        timer = findViewById(R.id.timer);

        // updates the timer for Minesweeper
        // This code was adapted from a post by waqaslam on 2018/11/29
        // Forum link: https://stackoverflow.com/questions/9850594/why-handler-timer-only-run-once
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String time = "Time: " + minesweeperGame.getTime();
                timer.setText(time);
                handler.postDelayed(this, 1000);
            }
        }, 1000);

        minesweeperGame.getBoard().addObserver(this);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / minesweeperGame.getNumCols();
                        columnHeight = (displayHeight - 200) / minesweeperGame.getNumRows();

                        display();
                    }
                });
    }

    /**
     * Adds the button that the user can press to switch between flagging mode and revealing mode
     */
    private void addFlagButton() {
        flagging = findViewById(R.id.flagging);

        int id = R.id.flagButton;
        Button flagButton = findViewById(id);
        flagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minesweeperGame.toggleFlagging();
                toggleFlaggingText();
            }
        });
    }

    /**
     * Sets text that tells user whether they are in flagging mode or revealing mode
     */
    private void toggleFlaggingText() {
        if (minesweeperGame.getFlagging()) {
            flagText = "Flagging";
            flagging.setText(flagText);
        } else {
            flagText = "Revealing";
            flagging.setText(flagText);
        }
    }

    /**
     * Adds text that tells user their time
     */
    private void addTimer() {
        timer = findViewById(R.id.timer);
    }

    /**
     * Adds the text that tells user number of bombs subtracting number of flags
     */
    private void addBombCounter() {
        bombCounter = findViewById(R.id.bombCounter);
    }

    /**
     * Updates the bomb counter every time the user adds or removes a flag.
     */
    private void updateBombCounter() {
        bombCounter.setText(String.valueOf(minesweeperGame.getBombsLeft()));
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        MSBoard board = minesweeperGame.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != minesweeperGame.getNumRows(); row++) {
            for (int col = 0; col != minesweeperGame.getNumCols(); col++) {
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
        MSBoard board = minesweeperGame.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / minesweeperGame.getNumRows();
            int col = nextPos % minesweeperGame.getNumCols();
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
                minesweeperGame = (MinesweeperGame) input.readObject();
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
            outputStream.writeObject(minesweeperGame);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Called when the display is to be updated (after a move is made)
     *
     * @param o   Observable
     * @param arg Object
     */
    @Override
    public void update(Observable o, Object arg) {
        updateTileButtons();
        updateBombCounter();
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

    /**
     * When the back button is pressed, the activity is switched to GameMenuActivity
     */

    @Override
    public void onBackPressed() {
        minesweeperGame.setSaveTime();
        switchToGMA();
    }

    /**
     * Switch to the GameOverActivity. Happens when the game ends.
     */
    public void switchToGameOver() {
        Intent gameOverIntent = new Intent(getApplicationContext(), GameOverActivity.class);

        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new MinesweeperGame(0, 0, 0));
        gmaBundle.putString("GAME_DESC", MinesweeperGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "MINESWEEPER");
        gmaBundle.putBoolean("DARKVIEW", DarkView);
        gameOverIntent.putExtras(gmaBundle);
        startActivity(gameOverIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    /**
     * Reveals all bombs once the user loses the game.
     */
    public void revealAllBombs() {
        GameOver = true;
        List<MSTile> tiles = new ArrayList<>();
        for (int i = 0; i < minesweeperGame.getNumRows(); i++) {
            for (int j = 0; j < minesweeperGame.getNumCols(); j++) {
                tiles.add(minesweeperGame.getBoard().getTile(i, j));
                System.out.println(i + "," + j);
            }
        }
        for (MSTile t : tiles)
            if (t.getId() == MSTile.MINE) {
                minesweeperGame.getBoard().revealTile(t);
                System.out.println(t.getId());
            }
    }
}
