
package fall2018.csc2017.game_centre.sliding_tiles;

import fall2018.csc2017.game_centre.*;

import java.io.Serializable;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class STTile implements Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The unique id.
     */
    private int id;

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
            R.drawable.tile_9,
            R.drawable.tile_10,
            R.drawable.tile_11,
            R.drawable.tile_12,
            R.drawable.tile_13,
            R.drawable.tile_14,
            R.drawable.tile_15,
            R.drawable.tile_16,
            R.drawable.tile_17,
            R.drawable.tile_18,
            R.drawable.tile_19,
            R.drawable.tile_20,
            R.drawable.tile_21,
            R.drawable.tile_22,
            R.drawable.tile_23,
            R.drawable.tile_24,
            R.drawable.tile_25,
    };

    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

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
     * @param backgroundId background ID
     * @param gameSize     size of game
     */
    public STTile(int backgroundId, int gameSize) {
        id = backgroundId + 1;
        if (id == gameSize * gameSize) {
            background = TILE_BACKGROUNDS[0];
        } else {
            background = TILE_BACKGROUNDS[id];
        }
    }
}
