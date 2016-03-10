package at.jku.tk.steinbauer.matthias.froyotictactoe.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;

import at.jku.tk.steinbauer.matthias.froyotictactoe.R;
import at.jku.tk.steinbauer.matthias.froyotictactoe.TicTacToeActivity;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.ServiceFactory;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.TicTacToeService;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Game;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Player;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by matthias on 09/03/16.
 */
public class CreateGameAsyncTask extends AsyncTask<Player, Integer, Game> {

    private TicTacToeActivity connectedActivity;

    public CreateGameAsyncTask(TicTacToeActivity activity) {
        this.connectedActivity = activity;
    }

    @Override
    protected Game doInBackground(Player... params) {
        Call<Game> game = ServiceFactory.getService().createGame(params[0]);
        try {
            Log.e("pre-rest", "body " + params);
            Game body = game.execute().body();
            Log.e("post-rest", "body " + body.getId());
            // ((TextView) findViewById(R.id.playerName)).setText("Call success");
            this.connectedActivity.restGameResult(body);
            return body;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
