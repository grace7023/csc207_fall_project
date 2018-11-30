package fall2018.csc2017.game_centre;

import fall2018.csc2017.game_centre.sliding_tiles.*;
import fall2018.csc2017.game_centre.twenty_forty.*;
import fall2018.csc2017.game_centre.minesweeper.*;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Displays the available games to the player
 */
public class GameManager extends AppCompatActivity {

    /**
     * The icon the player taps on to play sliding tiles
     */
    private ImageButton mSlidingTilesBtn;

    /**
     * Image button for 2048 game
     */

    private ImageButton mTwentyFortyBtn;

    /**
     * Image button for Minesweeper
     */

    private ImageButton mMinesweeperBtn;

    /**
     * Current user's username
     */

    private String currentUsername;

    /**
     * Layout for Game Manager
     */

    private ConstraintLayout gameManagerLayout;

    /**
     * Boolean value corresponding to whether the game is in dark mode or not"
     */
    private boolean darkView;

    /**
     * Creates the UI elements
     *
     * @param savedInstanceState A bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        mSlidingTilesBtn = findViewById(R.id.SlidingTiles);
        mTwentyFortyBtn = findViewById(R.id.TwentyForty);
        mMinesweeperBtn = findViewById(R.id.Minesweeper);
        darkView = false;
        currentUsername = getIntent().getStringExtra("USERNAME");
        darkView = getIntent().getBooleanExtra("DARKVIEW", false);
        gameManagerLayout = findViewById(R.id.gameManagerActivity);

        addSlidingTilesListener();
        addTFListener();
        addMinesweeperListener();
        addDarkButton();
        setUpDarkView();

    }

    /**
     * Provides functionality to the Minesweeper icon button
     */
    private void addMinesweeperListener() {
        mMinesweeperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent msGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
                Bundle gmaBundle = new Bundle();
                gmaBundle.putSerializable("GAME", new MinesweeperGame(0, 0, 0));
                gmaBundle.putString("GAME_DESC", MinesweeperGame.GAME_DESC);
                gmaBundle.putString("GAME_FILENAME", "Minesweeper_" + currentUsername);
                gmaBundle.putString("USERNAME", currentUsername);
                gmaBundle.putString("GAME_NAME", "MINESWEEPER");
                gmaBundle.putBoolean("DARKVIEW", darkView);
                msGMAIntent.putExtras(gmaBundle);
                startActivity(msGMAIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });
    }

    /**
     * Provides functionality to the 2048 icon button
     */
    private void addTFListener() {
        mTwentyFortyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tfGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
                Bundle gmaBundle = new Bundle();
                gmaBundle.putSerializable("GAME", new TFGame(4));
                gmaBundle.putString("GAME_DESC", TFGame.GAME_DESC);
                gmaBundle.putString("GAME_FILENAME", "TwentyForty_" + currentUsername);
                gmaBundle.putString("USERNAME", currentUsername);
                gmaBundle.putString("GAME_NAME", "2048");
                gmaBundle.putBoolean("DARKVIEW", darkView);
                tfGMAIntent.putExtras(gmaBundle);
                startActivity(tfGMAIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });
    }

    /**
     * Provides functionality to the sliding tiles icon button
     */
    private void addSlidingTilesListener() {
        mSlidingTilesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
                Bundle gmaBundle = new Bundle();
                gmaBundle.putSerializable("GAME", new SlidingTilesGame(0));
                gmaBundle.putString("GAME_DESC", SlidingTilesGame.GAME_DESC);
                gmaBundle.putString("GAME_FILENAME", "SlidingTiles_" + currentUsername);
                gmaBundle.putString("USERNAME", currentUsername);
                gmaBundle.putString("GAME_NAME", "SLIDING TILES");
                gmaBundle.putBoolean("DARKVIEW", darkView);
                stGMAIntent.putExtras(gmaBundle);
                startActivity(stGMAIntent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });
    }

    /**
     * Creates the actual dark mode button in the app
     */
    private void addDarkButton() {
        Button darkButton = findViewById(R.id.darkButton);
        darkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkView = !darkView;
                setUpDarkView();

            }
        });
    }

    /**
     * Puts the game into darkview by changing the background
     * colour to dark grey and the text colour to white. This applies to all
     * other screens of the app, not specifically this one.
     */
    private void setUpDarkView() {
        if (darkView) {
            gameManagerLayout.setBackgroundColor(Color.DKGRAY);
            TextView slidingTilesHeading = findViewById(R.id.SlidingTilesHeading);
            slidingTilesHeading.setTextColor(Color.WHITE);
            TextView minesweeperHeading = findViewById(R.id.minesweeperHeading);
            minesweeperHeading.setTextColor(Color.WHITE);
            TextView twentyfourtyHeading = findViewById(R.id.TwentyFortyHeading);
            twentyfourtyHeading.setTextColor(Color.WHITE);
            TextView title = findViewById(R.id.GameCentreTitle);
            title.setTextColor(Color.WHITE);
        } else {
            gameManagerLayout.setBackgroundColor(Color.WHITE);
            TextView slidingTilesHeading = findViewById(R.id.SlidingTilesHeading);
            slidingTilesHeading.setTextColor(Color.BLACK);
            TextView minesweeperHeading = findViewById(R.id.minesweeperHeading);
            minesweeperHeading.setTextColor(Color.BLACK);
            TextView twentyfourtyHeading = findViewById(R.id.TwentyFortyHeading);
            twentyfourtyHeading.setTextColor(Color.BLACK);
            TextView title = findViewById(R.id.GameCentreTitle);
            title.setTextColor(Color.BLACK);

        }
    }

}
