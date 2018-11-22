package fall2018.csc2017.game_centre.twenty_forty;

import java.io.Serializable;
import java.util.ArrayList;

import fall2018.csc2017.game_centre.Game;

public class TFGame extends Game implements Serializable {

    private int boardSize;

    private TFBoard board;

    private ArrayList<TFBoard> pastBoards;

    private int score;


    public TFGame(TFBoard board) {
//        this.boardSize = TFBoard.getBoardSize(); TODO: Implement getBoardSize for TFBoard
        this.board = board;
        this.score = 0;
    }

    public TFGame(int boardSize) {
        this.boardSize = boardSize;
        this.score = 0;
    }

    public TFBoard getBoard() { return board; }


    /**
     * Return whether a move with parameter arg is valid
     *
     * @param arg what argument representing the move attempted
     * @return whether the tap is valid
     */
    @Override
    public boolean isValidMove(int arg) {
        return false;
    }

    /**
     * Perform a move identified by arg.
     *
     * @param arg integer identifying the move
     */
    @Override
    public void move(int arg) {

    }

    /**
     * Return whether the Game is over.
     *
     * @return whether Game is over
     */
    @Override
    public boolean isOver() {
        return isStuck();
    }

    /**
     * Tells whether the tiles are stuck
     *
     * @return True iff tiles are not movable
     */
    private boolean isStuck() {
        //TODO: implement isStuck()
        return true;
    }

    /**
     * Undo function that reverses the most recent move.
     */
    @Override
    public void undo() {

    }

    /**
     * Size of the relevant screen real estate for game.
     */
    @Override
    public int getScreenSize() {
        return boardSize;
    }
}
