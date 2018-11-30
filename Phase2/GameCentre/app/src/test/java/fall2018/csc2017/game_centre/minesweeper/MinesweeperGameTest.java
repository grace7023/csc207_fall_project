package fall2018.csc2017.game_centre.minesweeper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MinesweeperGameTest {

    /**
     * Returns a List of MSTiles as an input for the board
     */
    private List<MSTile> makeTiles(int type, int numRows, int numCols, int numBombs) {
        List<MSTile> tiles = new ArrayList<>(numRows * numCols);
        for (int i = 0; i < numBombs; i++) {
            tiles.add(new MSTile(MSTile.MINE));
        }
        for (int i = numBombs; i < numRows * numCols; i++) {
            tiles.add(new MSTile(type));
        }
        return tiles;
    }

    /**
     * Returns a board with dimensions numRows*numCols with the first numBombs elements as mines
     */
    private MinesweeperGame setUpBoard(int type, int numRows, int numCols, int numBombs) {
        List<MSTile> tiles = makeTiles(type, numRows, numCols, numBombs);
        MSBoard board = new MSBoard(tiles, numRows, numCols);
        return new MinesweeperGame(board, numBombs);
    }

    /**
     * Returns a randomly scrambled board with dimensions numRows*numCols with numBombs mines
     */
    private MinesweeperGame setUpRandomBoard(int numRows, int numCols, int numBombs) {
        return new MinesweeperGame(numRows, numCols, numBombs);
    }

    /**
     * Tests whether the randomly scrambled board has the correct number of mines
     */
    @Test
    public void testGenerateBoard() {
        MinesweeperGame msGame = setUpRandomBoard(3, 3, 3);
        assertEquals(3, msGame.getNumBombs());
    }
    /**
     * Tests whether the game is over when all tiles are revealed
     */
    @Test
    public void isOverReveal() {
        MinesweeperGame msGame = setUpBoard(0,3, 3, 0);
        assertFalse(msGame.isOver());
        msGame.move(0);
        assertTrue(msGame.isOver());
    }

    /**
     * Tests whether the game is over when a bomb is clicked
     */
    @Test
    public void isOverBombClicked() {
        MinesweeperGame msGame = setUpBoard(0,3,3,3);
        assertFalse(msGame.isOver());
        msGame.move(0);
        assertTrue(msGame.isOver());
    }

    /**
     * Tests whether all the revealable tiles are revealed
     */
    @Test
    public void puzzleSolved() {
        MinesweeperGame msGame = setUpBoard(0,3, 3, 0);
        assertFalse(msGame.puzzleSolved());
        msGame.move(0);
        assertTrue(msGame.puzzleSolved());
    }

    /**
     * Tests whether the move made is valid or not
     */
    @Test
    public void isValidMove() {
        MinesweeperGame msGame = setUpBoard(1,3,3,5);
        assertTrue(msGame.isValidMove(5));
        msGame.move(5);

        msGame.toggleFlagging(); //flagging on
        msGame.move(8);
        assertTrue(msGame.isValidMove(5));

        assertTrue(msGame.isValidMove(6));
        msGame.move(6);
        assertTrue(msGame.isValidMove(6));

        msGame.move(8); // unflag 8
        msGame.toggleFlagging(); //flagging off
        msGame.move(7);

        assertFalse(msGame.isValidMove(8));

    }

    /**
     * Tests whether the game correctly returns whether you won or you lose
     */
    @Test
    public void gameOverText() {
        MinesweeperGame msGame1 = setUpBoard(1,1,1,1);
        msGame1.move(0);

        assertEquals("GAME OVER!", msGame1.gameOverText());

        MinesweeperGame msGame2 = setUpBoard(0,3,3,0);
        msGame1.move(0);

        assertEquals("YOU WIN!", msGame2.gameOverText());
    }

    /**
     * Tests whether making a move on a revealed tile properly reveals its adjacent tiles
     */
    @Test
    public void revealedTileMove() {
        List<MSTile> tiles = new ArrayList<>();
        tiles.add(new MSTile(MSTile.MINE));
        for (int i = 0; i < 3; i++) tiles.add(new MSTile(MSTile.EMPTY));
        tiles.add(new MSTile(1));
        for (int i = 0; i < 4; i++) tiles.add(new MSTile(MSTile.EMPTY));
        MSBoard board = new MSBoard(tiles, 3, 3);
        MinesweeperGame msGame = new MinesweeperGame(board, 1);

        msGame.move(4);
        msGame.toggleFlagging();
        msGame.move(0);
        msGame.move(4);
        assertTrue(msGame.puzzleSolved());
    }
}