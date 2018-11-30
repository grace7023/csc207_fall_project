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

    private Button gameoverButton;
    private String currentUsername;
    private String gameFilename;
    private Game game;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        addGameoverButton();
        gameFilename = getIntent().getStringExtra("GAME_FILENAME");
        currentUsername = getIntent().getStringExtra("USERNAME");
        game = (Game) getIntent().getSerializableExtra("GAME");
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
        Intent GMAIntent = new Intent(getApplicationContext(), GameMenuActivity.class);
        Bundle gmaBundle = new Bundle();
        gmaBundle.putSerializable("GAME", game);
        assert getIntent().getExtras() != null;
        gmaBundle.putString("GAME_DESC", getIntent().getExtras().getString("GAME_DESC"));
        gmaBundle.putString("GAME_FILENAME", gameFilename);
        gmaBundle.putString("USERNAME", currentUsername);
        gmaBundle.putString("GAME_NAME", getIntent().getExtras().getString("GAME_NAME"));
        GMAIntent.putExtras(gmaBundle);
        startActivity(GMAIntent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }
}
