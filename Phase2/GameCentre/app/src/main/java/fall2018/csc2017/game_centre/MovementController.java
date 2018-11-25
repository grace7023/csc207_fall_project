package fall2018.csc2017.game_centre;

import android.content.Context;
import android.widget.Toast;

import fall2018.csc2017.game_centre.minesweeper.MinesweeperGame;
import fall2018.csc2017.game_centre.sliding_tiles.SlidingTilesGame;


class MovementController {

    private Game game = null;

    MovementController() {
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


    void setGame(Game game){
        this.game = game;
    }
}
