package fall2018.csc2017.game_centre;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class GameOverActivity extends AppCompatActivity {

    /**
     * Create Button to be connected with GameOverButton
     */
    Button GameOverButton;

    /**
     * Name of current user
     */
    private String currentUsername;

    /**
     * Current game file name
     */
    private String gameFilename;

    private boolean DarkView;
    /**
     * Current Game
     */
    private Game game;
    TextView gameOverText;

    /**
     * Create UI for Game Over
     *
     * @param savedInstanceState a bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        addGameOverButton();
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        currentUsername = getIntent().getStringExtra("USERNAME");
        game = (Game) getIntent().getSerializableExtra("GAME");
        DarkView = getIntent().getBooleanExtra("DARKVIEW", false);

        gameOverText = findViewById(R.id.gameoverText);
        gameOverText.setText(game.gameOverText());

        setUpDarkview();
    }

    /**
     * Connect GameOverButton to actual button in activity
     */
    public void addGameOverButton() {
        GameOverButton = findViewById(R.id.gameoverButton);
        GameOverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGMA();
            }
        });
    }

    /**
     * Switch to Game Menu Activity
     */
    public void switchToGMA() {
        Intent GMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", game);
        assert getIntent().getExtras() != null;
        gmaBundle.putString("GAME_DESC", getIntent().getExtras().getString("GAME_DESC"));
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", getIntent().getExtras().getString("GAME_NAME"));
        gmaBundle.putBoolean("DARKVIEW", DarkView);
        GMAIntent.putExtras(gmaBundle);
        startActivity(GMAIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    private void setUpDarkview(){
        if (DarkView){
            ConstraintLayout constraintLayout = findViewById(R.id.gameOverActivity);
            constraintLayout.setBackgroundColor(Color.DKGRAY);
            TextView gameOverText = findViewById(R.id.gameOverText);
            gameOverText.setTextColor(Color.WHITE);
        }
    }
}
