package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the scoreboard for the user
 */
public class ScoreboardActivity extends AppCompatActivity {

    /**
     * The scoreboard to be displayed
     */
    public Scoreboard scoreboard;

    /**
     * All the scores for the current game being played
     */
    public List<Score> scores = new ArrayList<>();

    /**
     * User specific scores for the current game
     */
    public List<Score> playerScores = new ArrayList<>();

    /**
     * the game that the scoreboard is currently displaying scores for
     */
    private Game game;

    /**
     * the username of the current logged in user
     */
    private String currentUsername;

    /**
     * the save file directory of the game
     */
    private String gameFilename;

    /**
     * the description of the game
     */
    private String gameDesc;

    /**
     * name of the game
     */
    private String gameName;

    /**
     * the textview object that displays the title of the game in scoreboard
     */
    TextView scoreboardGameTitle;

    /**
     * Boolean value corresponding to whether the app is in dark mode or not
     */
    private boolean darkView;

    /**
     * Creates the UI elements
     *
     * @param savedInstanceState A bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Bundle gameBundle = getIntent().getExtras();
        assert gameBundle != null;
        game = (Game) gameBundle.getSerializable("GAME");
        currentUsername = gameBundle.getString("USERNAME");
        gameFilename = gameBundle.getString("GAME_FILENAME");
        gameDesc = gameBundle.getString("GAME_DESC");
        gameName = gameBundle.getString("GAME_NAME");
        darkView = gameBundle.getBoolean("DARKVIEW");
        String[] stringParts = gameFilename.split("_");
        scoreboard = new Scoreboard(stringParts[0]);
        scoreboard.loadFromFile();
        scores = scoreboard.getScores();
        playerScores = scoreboard.getScores(currentUsername);

        setUpScoreboardGameTitle();


        /* This code adapted from
         * https://stackoverflow.com/questions/4540754/dynamically-add-elements-to-a-listview-android
         */
        int scoreid1;
        int scoreid2;
        if (darkView) {
            scoreid1 = R.layout.white_spinner_item;
            scoreid2 = R.layout.white_spinner_item;
        } else {
            scoreid1 = android.R.layout.simple_list_item_1;
            scoreid2 = android.R.layout.simple_list_item_1;
        }



        ArrayAdapter<Score> scoreAdapter = new ArrayAdapter<>(
                this, scoreid1, scores);
        ArrayAdapter<Score> playerScoreAdapter = new ArrayAdapter<>(
                this, scoreid2, playerScores);


        ListView scoreListView = findViewById(R.id.scoreList);
        scoreListView.setAdapter(scoreAdapter);

        ListView playerScoreListView = findViewById(R.id.scoreList2);
        playerScoreListView.setAdapter(playerScoreAdapter);

        View headerView1 = getLayoutInflater().inflate(R.layout.header_item_1, null);
        View headerView2 = getLayoutInflater().inflate(R.layout.header_item_2, null);
        if (darkView) {
            scoreListView.addHeaderView(headerView1);
            playerScoreListView.addHeaderView(headerView2);
        } else {
            scoreListView.addHeaderView(formatText(
                    new TextView(this), "Total Highscores"));
            playerScoreListView.addHeaderView(formatText(
                    new TextView(this), "Player's Highscore"));
        }


        setUpDarkView();

    }

    /**
     * increases the size of the text of the header for the score lists
     *
     * @param textView the header view object
     * @param text     the text in the textView
     * @return the new modified textView
     */
    public TextView formatText(TextView textView, String text) {
        textView.setText(text);
        textView.setHeight(300);
        textView.setTextSize(30);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    /**
     * Displays the title of the scoreboard
     */
    public void setUpScoreboardGameTitle() {
        scoreboardGameTitle = findViewById(R.id.scoreboardGameTitle);
        scoreboardGameTitle.setText(gameName);
        scoreboardGameTitle.setTextSize(35);
    }

    /**
     * Switch to GameMenuActivity for Minesweeper. Pass the game, game description, game file name,
     * current username, and game name into the intent.
     */
    private void switchToGMA() {
        Intent tfGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", game);
        gmaBundle.putString("GAME_DESC", gameDesc);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", gameName);
        gmaBundle.putBoolean("DARKVIEW", darkView);
        tfGMAIntent.putExtras(gmaBundle);
        startActivity(tfGMAIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        switchToGMA();
    }

    /**
     * Puts the app into dark mode by setting the background colour
     * to dark grey and the text colour to white
     */
    private void setUpDarkView(){
        if (darkView){
            RelativeLayout relativeLayout = findViewById(R.id.scoreboardActivity);
            relativeLayout.setBackgroundColor(Color.DKGRAY);
            TextView scoreboardGameTitle = findViewById(R.id.scoreboardGameTitle);
            scoreboardGameTitle.setTextColor(Color.WHITE);
        }
    }
}
