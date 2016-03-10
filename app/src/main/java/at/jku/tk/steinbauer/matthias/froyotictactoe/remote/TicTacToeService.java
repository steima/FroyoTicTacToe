package at.jku.tk.steinbauer.matthias.froyotictactoe.remote;

import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Game;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Move;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Player;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by matthias on 09/03/16.
 */
public interface TicTacToeService {

    @POST("games")
    public Call<Game> createGame(@Body Player player);

    @POST("games/{gameId}/nextMove")
    public Call<Move> placeMove(@Body Move move, @Path("gameId") String gameId);

    @GET("games/{gameId}/nextMove")
    public Call<Move> awaitMove(@Path("gameId") String gameId);

}
