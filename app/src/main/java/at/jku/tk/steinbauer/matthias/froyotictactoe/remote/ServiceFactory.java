package at.jku.tk.steinbauer.matthias.froyotictactoe.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by matthias on 09/03/16.
 */
public class ServiceFactory {

    /**
     * Create and connect a serice
     *
     * @return
     */
    public static TicTacToeService getService() {
        OkHttpClient httpClient = new OkHttpClient.Builder().
                readTimeout(180, TimeUnit.SECONDS).
                connectTimeout(60, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("http://10.0.2.2:8080/tictactoe-rest/tictactoe/")
                .baseUrl("http://www.tk.jku.at/tictactoe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TicTacToeService ticTacToeService = retrofit.create(TicTacToeService.class);
        return ticTacToeService;
    }
}
