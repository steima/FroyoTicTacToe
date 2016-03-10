package at.jku.tk.steinbauer.matthias.froyotictactoe.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import at.jku.tk.steinbauer.matthias.froyotictactoe.TicTacToeActivity;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.ServiceFactory;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Move;
import retrofit2.Call;

/**
 * Created by matthias on 09/03/16.
 */
public class PlaceMoveAsyncTask extends AsyncTask<Object, Integer, Move> {

    private final TicTacToeActivity connectedActivity;

    public PlaceMoveAsyncTask(TicTacToeActivity activity) {
        this.connectedActivity = activity;
    }

    @Override
    protected Move doInBackground(Object... params) {
        String gameId = (String) params[0];
        Move move = (Move) params[1];
        Call<Move> moveCall = ServiceFactory.getService().placeMove(move, gameId);
        try {
            move = moveCall.execute().body();
            this.connectedActivity.restWaitForMoveCallback(gameId);
            return move;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
