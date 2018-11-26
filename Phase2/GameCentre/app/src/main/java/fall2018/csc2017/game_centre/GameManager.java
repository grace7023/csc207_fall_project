package fall2018.csc2017.game_centre;
import fall2018.csc2017.game_centre.sliding_tiles.*;
import fall2018.csc2017.game_centre.twenty_forty.*;
import fall2018.csc2017.game_centre.minesweeper.*;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Displays the available games to the player
 */
public class GameManager extends AppCompatActivity {

    /**
     * The icon the player taps on to play sliding tiles
     */
    private ImageButton mSlidingTilesBtn;

    private ImageButton mTwentyFortyBtn;

    private ImageButton mMinesweeperBtn;

    private String currentUsername;

    /**
     * Creates the UI elements
     * @param savedInstanceState A bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        mSlidingTilesBtn = findViewById(R.id.SlidingTiles);
        mTwentyFortyBtn = findViewById(R.id.TwentyForty);
        mMinesweeperBtn = findViewById(R.id.Minesweeper);

        currentUsername = getIntent().getStringExtra("USERNAME");

        addSlidingTilesListener();
        addTFListener();
        addMinesweeperListener();
    }

    /**
     * Provides functionality to the Minesweeper icon button
     */
    private void addMinesweeperListener() {
        mMinesweeperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stGameIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
                Bundle gameBundle = new Bundle();
                gameBundle.putSerializable("GAME", new MinesweeperGame(0, 0, 0));
                gameBundle.putString("GAME_DESC", MinesweeperGame.GAME_DESC);
                gameBundle.putString("GAME_FILENAME", currentUsername + "_" + "Minesweeper");
                stGameIntent.putExtras(gameBundle);
                startActivity(stGameIntent);
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
                Intent stGameIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
                Bundle gameBundle = new Bundle();
                gameBundle.putSerializable("GAME", new TFGame(0));
                gameBundle.putString("GAME_DESC", TFGame.GAME_DESC);
                gameBundle.putString("GAME_FILENAME", currentUsername + "_" + "TwentyForty");
                gameBundle.putString("USERNAME", currentUsername);
                stGameIntent.putExtras(gameBundle);
                startActivity(stGameIntent);
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
                Intent stGameIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
                Bundle gameBundle = new Bundle();
                gameBundle.putSerializable("GAME", new SlidingTilesGame(0));
                gameBundle.putString("GAME_DESC", SlidingTilesGame.GAME_DESC);
                gameBundle.putString("GAME_FILENAME", currentUsername + "_" + "SlidingTiles");
                gameBundle.putString("USERNAME", currentUsername);
                stGameIntent.putExtras(gameBundle);
                startActivity(stGameIntent);
            }
        });
    }
}
