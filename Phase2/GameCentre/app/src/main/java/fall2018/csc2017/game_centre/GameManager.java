package fall2018.csc2017.game_centre;
import fall2018.csc2017.game_centre.sliding_tiles.*;


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

    /**
     * Creates the UI elements
     * @param savedInstanceState A bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_manager);

        mSlidingTilesBtn = findViewById(R.id.SlidingTiles);

        addSlidingTilesListener();
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
