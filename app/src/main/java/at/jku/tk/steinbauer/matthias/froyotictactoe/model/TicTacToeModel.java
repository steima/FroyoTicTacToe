package at.jku.tk.steinbauer.matthias.froyotictactoe.model;

/**
 * Represents a single TicTacToe game
 *
 * Created by matthias on 08/03/16.
 */
public class TicTacToeModel {

    public static final int GAME_SIZE = 3;

    public enum TTTMark {
        Tic, Tac, None
    }

    private TTTMark currentPlayer;

    private TTTMark[][] field = new TTTMark[GAME_SIZE][GAME_SIZE];

    /**
     * Creates a new model for the local app
     */
    public TicTacToeModel() {
        this.resetField();
        this.currentPlayer = TTTMark.Tic;
    }

    /**
     * Sets all elements of the gamefield to None
     */
    public void resetField() {
        for(int row=0;row<GAME_SIZE;row++) {
            for(int col=0;col<GAME_SIZE;col++) {
                this.field[row][col] = TTTMark.None;
            }
        }
    }

    /**
     * Retrieve an element from the game
     *
     * @param row
     * @param col
     * @return
     */
    public TTTMark getMark(int row, int col) {
        return this.field[row][col];
    }

    /**
     * Mark a game field
     *
     * @param row
     * @param col
     */
    public boolean setMark(TTTMark mark, int row, int col) {
        TTTMark currentMark = getMark(row, col);
        if(currentMark != TTTMark.None) {
            return false;
        }else{
            this.field[row][col] = mark;
            if(this.currentPlayer == TTTMark.Tic) {
                this.currentPlayer = TTTMark.Tac;
            }else{
                this.currentPlayer = TTTMark.Tic;
            }
            return true;
        }
    }

    /**
     * Determines if there is a winner and returns the name if there is
     * one
     *
     * @return
     */
    public TTTMark getWinner() {
        for(int row=0;row<GAME_SIZE;row++) {
            if(this.field[row][0] != TTTMark.None && this.field[row][0] == this.field[row][1] && this.field[row][1] == this.field[row][2]) {
                return this.field[row][0];
            }
        }
        for(int col=0;col<GAME_SIZE;col++) {
            if(this.field[0][col] != TTTMark.None && this.field[0][col] == this.field[1][col] && this.field[1][col] == this.field[2][col]) {
                return this.field[col][0];
            }
        }
        if(this.field[1][1] != TTTMark.None) {
            if(this.field[0][0] == this.field[1][1] && this.field[1][1] == this.field[2][2]) {
                return this.field[1][1];
            }
            if(this.field[2][0] == this.field[1][1] && this.field[1][1] == this.field[0][2]) {
                return this.field[1][1];
            }
        }
        return TTTMark.None;
    }

    /**
     * Determines if there is no winner but all fields have been played
     *
     * @return
     */
    public boolean isDraw() {
        if(getWinner() == TTTMark.None) {
            for(int row=0;row<GAME_SIZE;row++) {
                for(int col=0;col<GAME_SIZE;col++) {
                    if(this.field[row][col] == TTTMark.None) {
                        return false;
                    }
                }
            }
            return true;
        }else {
            return false;
        }
    }

    public TTTMark getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(TTTMark currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

}
