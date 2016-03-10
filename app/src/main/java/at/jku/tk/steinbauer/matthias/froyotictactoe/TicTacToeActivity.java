package at.jku.tk.steinbauer.matthias.froyotictactoe;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import at.jku.tk.steinbauer.matthias.froyotictactoe.model.TicTacToeModel;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.TicTacToeService;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Game;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Move;
import at.jku.tk.steinbauer.matthias.froyotictactoe.remote.elems.Player;
import at.jku.tk.steinbauer.matthias.froyotictactoe.tasks.AwaitMoveAsyncTask;
import at.jku.tk.steinbauer.matthias.froyotictactoe.tasks.CreateGameAsyncTask;
import at.jku.tk.steinbauer.matthias.froyotictactoe.tasks.PlaceMoveAsyncTask;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TicTacToeActivity extends AppCompatActivity {

    private TicTacToeModel gameModel;

    private Game remoteGame;

    private Button[][] gameButtons = new Button[TicTacToeModel.GAME_SIZE][TicTacToeModel.GAME_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.gameModel = new TicTacToeModel();
        setContentView(R.layout.activity_tic_tac_toe);
        this.onResetClick(null);
    }

    /**
     * Is called when any of the TicTacToe buttons is clicked
     *
     * @param view
     */
    public void onClick(View view) {
        boolean success = false;
        int x = -1, y = -1;
        switch (view.getId()) {
            case R.id.ttb00:
                x = 0; y = 0;
                break;
            case R.id.ttb01:
                x = 0; y = 1;
                break;
            case R.id.ttb02:
                x = 0; y = 2;
                break;
            case R.id.ttb10:
                x = 1; y = 0;
                break;
            case R.id.ttb11:
                x = 1; y = 1;
                break;
            case R.id.ttb12:
                x = 1; y = 2;
                break;
            case R.id.ttb20:
                x = 2; y = 0;
                break;
            case R.id.ttb21:
                x = 2; y = 1;
                break;
            case R.id.ttb22:
                x = 2; y = 2;
                break;
        }
        success = this.gameModel.setMark(this.gameModel.getCurrentPlayer(), x, y);
        if(success && this.remoteGame != null) {
            Move move = new Move();
            move.setX(x);
            move.setY(y);
            this.enablePlayField(false);
            Toast.makeText(getApplicationContext(), "Warte auf den Zug des Gegners", Toast.LENGTH_LONG).show();
            new PlaceMoveAsyncTask(this).execute("" + this.remoteGame.getId(), move);
        }
        this.checkForWinner(success);
    }

    private void checkForWinner(boolean markResult) {
        if(markResult) {
            this.updateUiWithModel();
            TicTacToeModel.TTTMark winner = this.gameModel.getWinner();
            if(winner != TicTacToeModel.TTTMark.None) {
                String text = winner.toString() + " hat gewonnen!";
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }else{
                if(this.gameModel.isDraw()) {
                    Toast.makeText(getApplicationContext(), "Unentschieden!", Toast.LENGTH_LONG).show();
                }
            }
        }else{
            String text = "Das Feld war schon belegt!";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Is called when the reset button is clicked
     *
     * @param view
     */
    public void onResetClick(View view) {
        this.remoteGame = null;
        this.enablePlayField(true);
        this.gameModel.resetField();
        this.updateUiWithModel();
    }

    /**
     * Received information about the remote game
     *
     * @param game
     */
    public void restGameResult(Game game) {
        if(game != null) {
            Log.e("restGameResult", "Got a remote game " + game.getId());
            this.remoteGame = game;
            String me = ((TextView) findViewById(R.id.playerName)).getText().toString();
            String tmpOther = me;
            for (String cur : game.getPlayerNames()) {
                if (!cur.equals(me)) {
                    tmpOther = cur;
                }
            }
            final String other = tmpOther;
            final boolean weFirst = game.getPlayerNames()[0].equals(me);

            if (!weFirst) {
                restWaitForMoveCallback("" + game.getId());
            }

            final TicTacToeActivity tmpActivity = this;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tmpActivity.updateNewRestGameButton(true);
                    Toast.makeText(getApplicationContext(), "Du spielst gegen '" + other + "' " +
                                    (weFirst ? " du bist als erstes dran" : "dein Gegner ist als erstes dran"),
                            Toast.LENGTH_LONG).show();
                }
            });
        }else{
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Keinen Gegner gefunden",  Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * Received a move from the remote side
     *
     * @param move
     */
    public void restGameRemoteMove(Move move) {
        final boolean success = this.gameModel.setMark(this.gameModel.getCurrentPlayer(), move.getX(), move.getY());

        final TicTacToeActivity tmpActivity = this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tmpActivity.enablePlayField(true);
                tmpActivity.checkForWinner(success);
            }
        });
    }

    public void restWaitForMoveCallback(String gameId) {
        new AwaitMoveAsyncTask(this).execute(gameId);
    }

    /**
     * Initiates a new REST game
     *
     * @param view
     */
    public void onRestGameClick(View view) {
        this.onResetClick(view);
        this.updateNewRestGameButton(false);
        Player player = new Player();
        player.setPlayerName(((TextView) findViewById(R.id.playerName)).getText().toString());
        new CreateGameAsyncTask(this).execute(player);
        Toast.makeText(getApplicationContext(), "Warte auf einen würdigen Gegner", Toast.LENGTH_LONG).show();
    }

    /**
     * Updates the game button
     *
     * @param enabled
     */
    private void updateNewRestGameButton(boolean enabled) {
        Button button = (Button) findViewById(R.id.btnRemote);
        button.setEnabled(enabled);
    }

    /**
     * Updates the game field
     *
     * @param enabled
     */
    private void enablePlayField(boolean enabled) {
        this.updateGameButtonReferences();
        for(int row = 0;row<TicTacToeModel.GAME_SIZE;row++) {
            for(int col = 0;col<TicTacToeModel.GAME_SIZE;col++) {
                this.gameButtons[row][col].setEnabled(enabled);
            }
        }
    }

    /**
     * Retrieve references to the game buttons
     */
    private void updateGameButtonReferences() {
        this.gameButtons[0][0] = (Button) findViewById(R.id.ttb00);
        this.gameButtons[0][1] = (Button) findViewById(R.id.ttb01);
        this.gameButtons[0][2] = (Button) findViewById(R.id.ttb02);
        this.gameButtons[1][0] = (Button) findViewById(R.id.ttb10);
        this.gameButtons[1][1] = (Button) findViewById(R.id.ttb11);
        this.gameButtons[1][2] = (Button) findViewById(R.id.ttb12);
        this.gameButtons[2][0] = (Button) findViewById(R.id.ttb20);
        this.gameButtons[2][1] = (Button) findViewById(R.id.ttb21);
        this.gameButtons[2][2] = (Button) findViewById(R.id.ttb22);
    }

    /**
     * Reflect the current model state on the UI
     */
    public void updateUiWithModel() {
        this.updateGameButtonReferences();
        for(int row=0;row<TicTacToeModel.GAME_SIZE;row++) {
            for(int col=0;col<TicTacToeModel.GAME_SIZE;col++) {
                TicTacToeModel.TTTMark tmp = this.gameModel.getMark(row, col);
                if(tmp == null) {
                    Log.e("ttmark null", "row: " + row + " col: " + col);
                }
                switch (this.gameModel.getMark(row, col)) {
                    case None:
                        this.gameButtons[row][col].setText("_");
                        break;
                    case Tic:
                        this.gameButtons[row][col].setText("X");
                        break;
                    case Tac:
                        this.gameButtons[row][col].setText("O");
                        break;
                }
            }
        }
     }
}
