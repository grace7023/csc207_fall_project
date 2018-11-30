package fall2018.csc2017.game_centre;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.game_centre.minesweeper.MinesweeperGame;

public class GameoverActivity extends AppCompatActivity{

    private TextView gameoverText;
    private Button gameoverButton;
    private String currentUsername;
    private String gameFilename;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        addGameoverText();
        addGameoverButton();

        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        currentUsername = getIntent().getStringExtra("USERNAME");
    }

    public void addGameoverText(){
        gameoverText = findViewById(R.id.gameoverText);
        gameoverText.setTextSize(40);

    }
    public void addGameoverButton(){
        gameoverButton = findViewById(R.id.gameoverButton);
        gameoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGMA();
            }});
    }

    public void switchToGMA(){
        Intent msGMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", new MinesweeperGame(0, 0, 0));
        gmaBundle.putString("GAME_DESC", MinesweeperGame.GAME_DESC);
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", "MINESWEEPER");
        msGMAIntent.putExtras(gmaBundle);
        startActivity(msGMAIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }
}
