
package fall2018.csc2017.game_centre.minesweeper;

import java.io.Serializable;

import fall2018.csc2017.game_centre.R;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile implements Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

    private boolean revealed;

    private boolean flagged;

    public final static int EMPTY = 0;
    public final static int BOMB = 9;
    public final static int FLAGGED = 10;
    public final static int UNREVEALED = 11;
    /**
     * An array of all tile backgrounds.
     */
    private final static int[] TILE_BACKGROUNDS = {
            R.drawable.mine_0,
            R.drawable.mine_1,
            R.drawable.mine_2,
            R.drawable.mine_3,
            R.drawable.mine_4,
            R.drawable.mine_5,
            R.drawable.mine_6,
            R.drawable.mine_7,
            R.drawable.mine_8,
    };

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * Constructor for tile
     *
     * @param id The id of this tile
     */
    public Tile(int id) {
        this.id = id;
        revealed = false;
        flagged = false;
        background = TILE_BACKGROUNDS[UNREVEALED];
    }

    public void setId(int id) {
        this.id = id;
    }

    public void reveal() {
        background = TILE_BACKGROUNDS[id];
        revealed = true;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void toggleFlag() {
        if (!flagged) {
            background = TILE_BACKGROUNDS[FLAGGED];
            flagged = true;
        } else {
            background = TILE_BACKGROUNDS[UNREVEALED];
            flagged = false;
        }
    }

    public boolean isFlagged() {
        return flagged;
    }

    public int getBackground() { return background; }
}
