
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

    private final static int EMPTY = 0;
    private final static int BOMB = 9;
    private final static int FLAGGED = 10;
    private final static int UNREVEALED = 11;
    /**
     * An array of all tile backgrounds.
     */
    private final static int[] TILE_BACKGROUNDS = {
            R.drawable.tile_0,
            R.drawable.tile_1,
            R.drawable.tile_2,
            R.drawable.tile_3,
            R.drawable.tile_4,
            R.drawable.tile_5,
            R.drawable.tile_6,
            R.drawable.tile_7,
            R.drawable.tile_8,
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
     */
    public Tile() {
        background = TILE_BACKGROUNDS[UNREVEALED];
    }

    public void setId(int id) {
        this.id = id;
    }

    public void reveal() {
        background = TILE_BACKGROUNDS[id];
    }
}
