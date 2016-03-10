package at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems;

/**
 * Created by matthias on 09/03/16.
 */
public class Game {

    private long id;

    private String[] playerNames;

    private int numberOfRegisteredPlayers;

    private Move nextMove;

    public Move getNextMove() {
        return nextMove;
    }

    public void setNextMove(Move nextMove) {
        this.nextMove = nextMove;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public int getNumberOfRegisteredPlayers() {
        return numberOfRegisteredPlayers;
    }

    public void setNumberOfRegisteredPlayers(int numberOfRegisteredPlayers) {
        this.numberOfRegisteredPlayers = numberOfRegisteredPlayers;
    }


}
