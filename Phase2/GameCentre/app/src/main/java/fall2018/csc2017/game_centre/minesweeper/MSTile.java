
package fall2018.csc2017.game_centre.minesweeper;

import java.io.Serializable;

import fall2018.csc2017.game_centre.R;

/**
 * A Tile in a minesweeper game.
 */
public class MSTile implements Serializable {

    /**
     * The background id to find the tile image.
     */
    private int background;

    /**
     * The id of this tile from 0-11.
     */
    private int id;

    /**
     * If this tile is revealed or not
     */
    private boolean revealed;

    /**
     * If this tile has been flagged or not
     */
    private boolean flagged;

    /**
     * This tile is the empty tile with 0 mines around it
     */
    final static int EMPTY = 0;
    /**
     * This tile is the mine tile
     */
    final static int MINE = 9;
    /**
     * This tile has been flagged
     */
    private final static int FLAGGED = 10;
    /**
     * This tile is still unrevealed
     */
    private final static int UNREVEALED = 11;
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
            R.drawable.mine_mine,
            R.drawable.mine_flagged,
            R.drawable.mine_unrevealed
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
    MSTile(int id) {
        this.id = id;
        revealed = false;
        flagged = false;
        background = TILE_BACKGROUNDS[UNREVEALED];
    }

    /**
     * Sets the id of the tile
     *
     * @param id a value from 0-11 as the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Changes the background image of this tile from unrevealed to whatever image the ID represents
     */
    void reveal() {
        background = TILE_BACKGROUNDS[id];
        revealed = true;
    }

    /**
     * Returns if this tile has been revealed or not
     *
     * @return true if tile is revealed, false if not
     */
    boolean isRevealed() {
        return revealed;
    }

    /**
     * Changes the background from either unflagged to flagged or flagged to unflagged
     */
    void toggleFlag() {
        if (!flagged) {
            background = TILE_BACKGROUNDS[FLAGGED];
            flagged = true;
        } else {
            background = TILE_BACKGROUNDS[UNREVEALED];
            flagged = false;
        }
    }

    /**
     * Returns whether the tile is flagged or not
     *
     * @return true if the tile is flagged, false if not
     */
    boolean isFlagged() {
        return flagged;
    }

    /**
     * Gets the index of the background image of this tile
     *
     * @return index of the background image
     */
    public int getBackground() {
        return background;
    }
}
