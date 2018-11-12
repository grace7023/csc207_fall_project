package fall2018.csc2017.game_centre;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Observable;

public abstract class GameActivity extends AppCompatActivity {
    public abstract void display();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onPause() {
        super.onPause();
    }

    public abstract void loadFromFile(String fileName);

    public abstract void saveToFile(String fileName);


    /**
     * Auto save function that saves Game after each move.
     */
    public abstract void autoSave();
}
