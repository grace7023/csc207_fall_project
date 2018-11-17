package fall2018.csc2017.game_centre;
import fall2018.csc2017.game_centre.sliding_tiles.*;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    private ImageButton mMinesweeperBtm;

    /**
     * Creates the UI elements
     * @param savedInstanceState A bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        mSlidingTilesBtn = findViewById(R.id.SlidingTiles);
//        mTwentyFortyBtn = findViewById(R.id.TwentyForty); TODO: Create 2048 button in activity_game_manager.xml
//        mMinesweeperBtn = findViewById(R.id.Minesweeper); TODO: Create Minesweeper button in activity_game_manager.xml

        addSlidingTilesListener();
        addTFListener();
        addMinesweeperListener();
    }

    /**
     * Provides functionality to the Minesweeper icon button
     */
    private void addMinesweeperListener() {
        mMinesweeperBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameMenuActivity.GAME_DESC = ""; //TODO: Fill Game Desc for Minesweeper
                GameMenuActivity.filename = LogInScreen.currentUsername + "_" + "Minesweeper";
//                GameMenuActivity.GAME = new MinesweeperGame(); TODO: Implement MinesweeperGame
                startActivity(new Intent(GameManager.this, GameMenuActivity.class));
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
                GameMenuActivity.GAME_DESC = ""; //TODO: Fill Game Desc out for 2048
                GameMenuActivity.filename = LogInScreen.currentUsername + "_" + "TwentyForty";
//                GameMenuActivity.GAME = new TwentyFortyGame(); TODO: Implement TwentyFortyGame
                startActivity(new Intent(GameManager.this, GameMenuActivity.class));
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
                GameMenuActivity.GAME_DESC = "Welcome To Sliding Tiles \n A Puzzle Game where you" +
                        " must arrange the numbers in the correct order";
                GameMenuActivity.filename = LogInScreen.currentUsername + "_" + "SlidingTiles";
                GameMenuActivity.GAME = new SlidingTilesGame(0);
                startActivity(new Intent(GameManager.this, GameMenuActivity.class));
            }
        });
    }
}
