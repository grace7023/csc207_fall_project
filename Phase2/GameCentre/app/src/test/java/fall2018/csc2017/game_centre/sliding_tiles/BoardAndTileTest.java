package fall2018.csc2017.game_centre.sliding_tiles;

import fall2018.csc2017.game_centre.sliding_tiles.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardAndTileTest {

    /**
     * The board manager for testing.
     */
    private SlidingTilesGame slidingTilesGame;

    /**
     * Make a set of tiles that are in order.
     *
     * @return a set of tiles that are in order
     */
    private List<STTile> makeTiles(int sideLength) {
        List<STTile> tiles = new ArrayList<>();
        final int numTiles = sideLength * sideLength;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new STTile(tileNum, sideLength));
        }

        return tiles;
    }

    /**
     * Make a solved Board of with sideLength .
     */
    private void setUpCorrect(int sideLength) {
        List<STTile> tiles = makeTiles(sideLength);
        STBoard board = new STBoard(tiles, sideLength);
        slidingTilesGame = new SlidingTilesGame(board);
    }

    /**
     * Shuffle a few tiles.
     */
    private void swapFirstTwoTiles() {
        slidingTilesGame.getBoard().swapTiles(0, 0, 0, 1);
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved.
     */
    @Test
    public void testIsSolved() {
        setUpCorrect(3);
        assertTrue(slidingTilesGame.puzzleSolved());
        swapFirstTwoTiles();
        assertFalse(slidingTilesGame.puzzleSolved());
        setUpCorrect(4);
        assertTrue(slidingTilesGame.puzzleSolved());
        swapFirstTwoTiles();
        assertFalse(slidingTilesGame.puzzleSolved());
        setUpCorrect(5);
        assertTrue(slidingTilesGame.puzzleSolved());
        swapFirstTwoTiles();
        assertFalse(slidingTilesGame.puzzleSolved());
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo3() {
        setUpCorrect(3);
        assertEquals(1, slidingTilesGame.getBoard().getTile(0, 0).getId());
        assertEquals(2, slidingTilesGame.getBoard().getTile(0, 1).getId());
        slidingTilesGame.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(2, slidingTilesGame.getBoard().getTile(0, 0).getId());
        assertEquals(1, slidingTilesGame.getBoard().getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo3() {
        setUpCorrect(3);
        assertEquals(8, slidingTilesGame.getBoard().getTile(2, 1).getId());
        assertEquals(9, slidingTilesGame.getBoard().getTile(2, 2).getId());
        slidingTilesGame.getBoard().swapTiles(2, 1, 2, 2);
        assertEquals(9, slidingTilesGame.getBoard().getTile(2, 1).getId());
        assertEquals(8, slidingTilesGame.getBoard().getTile(2, 2).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap3() {
        setUpCorrect(3);
        assertTrue(slidingTilesGame.isValidMove(5));
        assertTrue(slidingTilesGame.isValidMove(7));
        assertFalse(slidingTilesGame.isValidMove(3));
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo4() {
        setUpCorrect(4);
        assertEquals(1, slidingTilesGame.getBoard().getTile(0, 0).getId());
        assertEquals(2, slidingTilesGame.getBoard().getTile(0, 1).getId());
        slidingTilesGame.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(2, slidingTilesGame.getBoard().getTile(0, 0).getId());
        assertEquals(1, slidingTilesGame.getBoard().getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo4() {
        setUpCorrect(4);
        assertEquals(15, slidingTilesGame.getBoard().getTile(3, 2).getId());
        assertEquals(16, slidingTilesGame.getBoard().getTile(3, 3).getId());
        slidingTilesGame.getBoard().swapTiles(3, 3, 3, 2);
        assertEquals(16, slidingTilesGame.getBoard().getTile(3, 2).getId());
        assertEquals(15, slidingTilesGame.getBoard().getTile(3, 3).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap4() {
        setUpCorrect(4);
        assertTrue(slidingTilesGame.isValidMove(11));
        assertTrue(slidingTilesGame.isValidMove(14));
        assertFalse(slidingTilesGame.isValidMove(10));
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo5() {
        setUpCorrect(5);
        assertEquals(1, slidingTilesGame.getBoard().getTile(0, 0).getId());
        assertEquals(2, slidingTilesGame.getBoard().getTile(0, 1).getId());
        slidingTilesGame.getBoard().swapTiles(0, 0, 0, 1);
        assertEquals(2, slidingTilesGame.getBoard().getTile(0, 0).getId());
        assertEquals(1, slidingTilesGame.getBoard().getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo5() {
        setUpCorrect(5);
        assertEquals(24, slidingTilesGame.getBoard().getTile(4, 3).getId());
        assertEquals(25, slidingTilesGame.getBoard().getTile(4, 4).getId());
        slidingTilesGame.getBoard().swapTiles(4, 4, 4, 3);
        assertEquals(25, slidingTilesGame.getBoard().getTile(4, 3).getId());
        assertEquals(24, slidingTilesGame.getBoard().getTile(4, 4).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap5() {
        setUpCorrect(5);
        assertTrue(slidingTilesGame.isValidMove(23));
        assertTrue(slidingTilesGame.isValidMove(19));
        assertFalse(slidingTilesGame.isValidMove(18));
    }
}

