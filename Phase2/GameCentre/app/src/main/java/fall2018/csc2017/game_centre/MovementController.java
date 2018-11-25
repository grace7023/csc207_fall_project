package fall2018.csc2017.game_centre;

import android.content.Context;
import android.widget.Toast;


class MovementController<T extends Game> {

    private T game = null;

    MovementController() {
    }

    void setGame(T game) {
        this.game = game;
    }

    void processGameTapMovement(Context context, int position) {
        if (game.isValidMove(position)) {
            game.move(position);
            if (game.isOver()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
