package fall2018.csc2017.game_centre;

import android.content.Context;
import android.widget.Toast;

import fall2018.csc2017.game_centre.sliding_tiles.SlidingTilesGame;


class MovementController {

    private SlidingTilesGame slidingTilesGame = null;

    MovementController() {
    }

    void setSlidingTilesGame(SlidingTilesGame slidingTilesGame) {
        this.slidingTilesGame = slidingTilesGame;
    }

    void processGameTapMovement(Context context, int position) {
        if (slidingTilesGame.isValidTap(position)) {
            slidingTilesGame.touchMove(position);
            if (slidingTilesGame.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
