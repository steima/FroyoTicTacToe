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
public class AwaitMoveAsyncTask extends AsyncTask<String, Integer, Move> {

    private TicTacToeActivity connectedActivity;

    public AwaitMoveAsyncTask(TicTacToeActivity activity) {
        this.connectedActivity = activity;
    }

    @Override
    protected Move doInBackground(String... params) {
        Call<Move> moveBody = ServiceFactory.getService().awaitMove(params[0]);
        try {
            Move move = moveBody.execute().body();
            this.connectedActivity.restGameRemoteMove(move);
            return move;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
