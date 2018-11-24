package fall2018.csc2017.game_centre.twenty_forty;

import fall2018.csc2017.game_centre.*;

import android.content.Context;
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
    // private SlidingTilesGame slidingTilesGame; TODO: Replicate. Create "TFGame"


    /**
     * The buttons to display.
     */
    private ArrayList<Button> boxButtons; //TODO: TileButtons ? Need to change this to fit 2048


    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView; //TODO: add functions for GestureDetecting Class to fit 2048: Swiping (OnFling)

    private static int columnWidth, columnHeight;

    /**
     * TextView for currentScore
     */
    private TextView currentScore; //TODO: Counting score for 2048


    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateScore();
        updateBoxButtons();
        gridView.setAdapter(new CustomAdapter(boxButtons, columnWidth, columnHeight));
        autoSave();
        // TODO: replicate this for 2048
//        if (slidingTilesGame.isOver()) {
//            Scoreboard scoreboard = Scoreboard.loadFromFile();
//            if (scoreboard == null) {
//                scoreboard = new Scoreboard();
//            }
//            scoreboard.addScore(LogInScreen.currentUsername, slidingTilesGame.getScore());
//            scoreboard.saveToFile();
//
//            startActivity(new Intent(this, GameMenuActivity.class));
//        }
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
        createBoxButtons(this);
        setContentView(R.layout.activity_main);
        addUndoButton();
        addCurrentScore();
        // Add View to activity
        //TODO: fix this mumbojumbo
//        gridView = findViewById(R.id.grid);
//        gridView.setNumColumns(slidingTilesGame.getScreenSize());
//        gridView.setSlidingTilesGame(slidingTilesGame);
//        slidingTilesGame.getBoard().addObserver(this);
//        // Observer sets up desired dimensions as well as calls our display function
//        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
//                                this);
//                        int displayWidth = gridView.getMeasuredWidth();
//                        int displayHeight = gridView.getMeasuredHeight();
//
//                        columnWidth = displayWidth / slidingTilesGame.getScreenSize();
//                        columnHeight = (displayHeight - 200) / slidingTilesGame.getScreenSize();
//
//                        display();
//                    }
//                });
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createBoxButtons(Context context) {
//        TODO: MUMBOJUMBO (createBoxButtons)
//        Board board = slidingTilesGame.getBoard();
//        boxButtons = new ArrayList<>();
//        for (int row = 0; row != slidingTilesGame.getScreenSize(); row++) {
//            for (int col = 0; col != slidingTilesGame.getScreenSize(); col++) {
//                Button tmp = new Button(context);
//                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
//                this.boxButtons.add(tmp);
//            }
//        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateBoxButtons() {
//        TODO: JUMBO (updateBoxButtons)
//        Board board = slidingTilesGame.getBoard();
//        int nextPos = 0;
//        for (Button b : boxButtons) {
//            int row = nextPos / slidingTilesGame.getScreenSize();
//            int col = nextPos % slidingTilesGame.getScreenSize();
//            b.setBackgroundResource(board.getTile(row, col).getBackground());
//            nextPos++;
//        }
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
//                TwentyFortyGame = (TwentyFortyGame) input.readObject(); TODO: Implement TwentyFortyGame
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
//        } catch (ClassNotFoundException e) {
//            Log.e("login activity", "File contained unexpected data type: " + e.toString());
//        }
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
//            outputStream.writeObject(TwentyFortyGame); TODO: Implement TwentyFortyGame
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

    /**
     * Add undo button listener.
     */

    public void addUndoButton() {
        Button undoButton = findViewById(R.id.undoButton);
//            TODO: JIMOTHY (undoButton)
            undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TFGame.canUndoMove()) {
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
//            TODO: fix UpdateScore for 2048
//            String newScore = "Score: " + String.valueOf(TwentyFortyGame.getScore());
//            currentScore.setText(newScore);
    }
}
