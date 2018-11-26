package fall2018.csc2017.game_centre.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Calendar;
import java.util.Timer;

import fall2018.csc2017.game_centre.CustomAdapter;
import fall2018.csc2017.game_centre.GameActivity;
import fall2018.csc2017.game_centre.GameMenuActivity;
import fall2018.csc2017.game_centre.GestureDetectGridView;
import fall2018.csc2017.game_centre.LogInScreen;
import fall2018.csc2017.game_centre.R;
import fall2018.csc2017.game_centre.Scoreboard;
import fall2018.csc2017.game_centre.sliding_tiles.SlidingTilesGameActivity;

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
    private ArrayList<Button> tileButtons;

    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView<MinesweeperGame> gridView;
    private static int columnWidth, columnHeight;


    /**
     * TextView for currentScore
     */
    private TextView timer;
    private TextView bombCounter;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTimer();
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
        autoSave();
        if (minesweeperGame.isOver()) {
            Scoreboard scoreboard = Scoreboard.loadFromFile();
            if (scoreboard == null) {
                scoreboard = new Scoreboard();
            }
            scoreboard.addScore(LogInScreen.currentUsername, minesweeperGame.getTime());
            scoreboard.saveToFile();

            startActivity(new Intent(this, GameMenuActivity.class));

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
        loadFromFile(GameMenuActivity.filename);
        createTileButtons(this);
//        addFlagButton();
        setContentView(R.layout.activity_minesweeper_game);
        addTimer();
        addBombCounter();
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(minesweeperGame.getNumCols());
        gridView.setGame(minesweeperGame);
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

//    private void addFlagButton() {
//        Button flagButton = findViewById(R.id.flagButton);
//        flagButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                minesweeperGame.toggleFlagging();
//            }
//        });
//    }


//    intent@5786
//    bundle@5783 outside intent
//    bundle@5803 inside intent
    private void addTimer(){
        timer = findViewById(R.id.timer);
    }

    private void addBombCounter(){
        bombCounter = findViewById(R.id.bombCounter);
        bombCounter.setText(String.valueOf(minesweeperGame.getNumBombs()));
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = minesweeperGame.getBoard();
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
        Board board = minesweeperGame.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            if (nextPos == 9){
                System.out.println();
            }
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

    @Override
    public void update(Observable o, Object arg) {
        display();
    }

    /**
     * Auto save function that saves Game after each move.
     */
    public void autoSave() {
        saveToFile(GameMenuActivity.filename);
    }

    private void updateTimer() {
        String newTime = minesweeperGame.getTime();
        timer.setText(newTime);
    }
}
