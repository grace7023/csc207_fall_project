package fall2018.csc2017.game_centre.minesweeper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MSBoardTest {
    MinesweeperGame minesweeperGame;

    /**
     * Makes a list of tiles as an input for the board
     * @return a List of MSTiles as an input for the board
     */
    private List<MSTile> makeTiles(int numRows, int numCols, int numBombs) {
        List<MSTile> tiles = new ArrayList<>(numRows * numCols);
        for (int i = 0; i < numBombs; i++) {
            tiles.add(new MSTile(MSTile.MINE));
        }
        for (int i = numBombs; i < numRows * numCols; i++) {
            tiles.add(new MSTile(0));
        }
        return tiles;
    }

    /**
     * Makes a board with dimensions numRows*numCols with the first numBombs elements as mines
     */
    private void setUpBoard(int numRows, int numCols, int numBombs) {
        List<MSTile> tiles = makeTiles(numRows, numCols, numBombs);
        MSBoard board = new MSBoard(tiles, numRows, numCols);
        minesweeperGame = new MinesweeperGame(board, numBombs);
    }

    /**
     * Tests whether the board returns the correct number of revealed tiles
     */
    @Test
    public void getNumRevealed() {
        setUpBoard(3,3,0);
        MSBoard board = minesweeperGame.getBoard();

        board.revealTile(board.getTile(0,0));
        assertEquals(1, board.getNumRevealed());

        for (int i = 0; i < board.getNumRows(); i++) {
            for (int j = 0; j < board.getNumCols(); j++) {
                board.revealTile(board.getTile(i,j));
            }
        }
        assertEquals(board.getBoardSize(), board.getNumRevealed());
    }

    /**
     * Tests whether a tile is correctly flagged
     */
    @Test
    public void toggleFlag() {
        setUpBoard(2,3,3);
        MSBoard board = minesweeperGame.getBoard();

        board.toggleFlag(1,1);

        assertTrue(board.getTile(1,1).isFlagged());
        assertFalse(board.getTile(1,2).isFlagged());

        board.getTile(1,1).toggleFlag();
        assertFalse(board.getTile(1,1).isFlagged());

    }

    /**
     * Tests whether the position of any tile on the board is correctly returned
     */
    @Test
    public void getPosition() {
        setUpBoard(3,3,3);
        MSBoard board = minesweeperGame.getBoard();

        assertEquals(4, board.getPosition(board.getTile(1,1)));

        MSTile tile = new MSTile(MSTile.EMPTY);
        assertEquals(-1, board.getPosition(tile));
    }

    /**
     *  Tests whether the iterator throws a NoSuchElementException if given bad tiles
     */
    @Test(expected = NoSuchElementException.class)
    public void testIterator() {
        setUpBoard(2,2,3);
        MSBoard board = minesweeperGame.getBoard();
        Iterator<MSTile> tileIterator = board.iterator();
        MSTile[][] emptyBoard = new MSTile[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                emptyBoard[row][col] = tileIterator.next();
            }
        }
    }
}