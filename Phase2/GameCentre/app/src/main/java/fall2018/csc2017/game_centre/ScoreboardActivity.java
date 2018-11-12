package fall2018.csc2017.game_centre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

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
    public ArrayList<Score> scores = new ArrayList<>();

    /**
     * User specific scores for the current game
     */
    public ArrayList<Score> playerScores = new ArrayList<>();

    /**
     * Creates the UI elements
     * @param savedInstanceState A bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreboard = Scoreboard.loadFromFile();
        scores = scoreboard.getScores();
        playerScores = scoreboard.getScores(LogInScreen.currentUsername);

        /*This code adapted from
        * https://stackoverflow.com/questions/4540754/dynamically-add-elements-to-a-listview-android
         */
        ArrayAdapter<Score> scoreAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, scores);
        ArrayAdapter<Score> playerScoreAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, playerScores);


        ListView scoreListView = findViewById(R.id.scoreList);
        scoreListView.setAdapter(scoreAdapter);

        ListView playerScoreListView = findViewById(R.id.scoreList2);
        playerScoreListView.setAdapter(playerScoreAdapter);


        scoreListView.addHeaderView(formatText(
                new TextView(this), "Total Highscores"));
        playerScoreListView.addHeaderView(formatText(
                new TextView(this), "Player's Highscore"));

    }

    /**
     * increases the size of the text of the header for the score lists
     * @param textView the header view object
     * @param text the text in the textView
     * @return the new modified textView
     */
    public TextView formatText(TextView textView, String text){
        textView.setText(text);
        textView.setHeight(300);
        textView.setTextSize(30);
        return textView;
    }
}
