package com.red.justcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.red.justcode.tengine.HPlayer;
import com.red.justcode.tengine.MlPlayer;
import com.red.justcode.tengine.Player;
import com.red.justcode.tengine.TGame;
import com.red.justcode.learning.TestTraining;

public class TttActivity extends AppCompatActivity implements View.OnClickListener {

    int i, j;
    View b[][];
    Player mPlayer1, mPlayer2;
    TextView mStatusView;
    Button mBtn_HvH, mBtn_Mvh;

    TGame mTgame;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttt);
        mStatusView = (TextView) findViewById(R.id.status);
        mTgame = new TGame(this);
        mBtn_HvH = (Button) findViewById(R.id.hvh);
        mBtn_Mvh = (Button) findViewById(R.id.mvh);
        mBtn_HvH.setOnClickListener(this);
        mBtn_Mvh.setOnClickListener(this);
		//MANI For testing purpose, others can comment below code
        //TestTraining tt = new TestTraining();
        //tt.dummyTest(this);
        //tt.neuralNetworkTests(this);
        //MANI End
    }

    public void nextmove() {
        boolean[] result = mTgame.isGameOver();
        if (mTgame.getCurrentPlayer().hashCode() == mPlayer1.hashCode()) {
            if (result[0]) {
                mBtn_Mvh.setEnabled(true);
                mBtn_HvH.setEnabled(true);
                if (result[1]) {
                    mStatusView.setText(getResources().getString(R.string.game_draw));
                } else {
                    mStatusView.setText(getResources().getString(R.string.player_2_won));
                }
                removeListener();
            } else {
                mStatusView.setText(getResources().getString(R.string.player_1_move));
            }
        } else {
            if (result[0]) {
                mBtn_Mvh.setEnabled(true);
                mBtn_HvH.setEnabled(true);
                removeListener();
                if (result[1]) {
                    mStatusView.setText(getResources().getString(R.string.game_draw));
                } else
                    mStatusView.setText(getResources().getString(R.string.player_1_won));
            } else {
                mStatusView.setText(getResources().getString(R.string.player_2_move));
            }
        }
    }

    private void removeListener() {
        mTgame = new TGame(this);
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                b[i][j].setOnClickListener(null);
                b[i][j].setEnabled(false);
            }
        }
    }

    public void nextmove(int pos) {
        if(pos == -1) {
            return;
        }
        int x = (pos / 3);
        int y = (pos - 3 * x) + 1;
        x = x + 1;
        if (b[x][y].isEnabled()) {
            if (mTgame.getCurrentPlayer().hashCode() == mPlayer1.hashCode()) {
                mStatusView.setText(getResources().getString(R.string.player_1_move));
                b[x][y].setEnabled(false);
                b[x][y].setBackground(getResources().getDrawable(R.drawable.o));
                mTgame.nextMove(3*(x-1)+(y-1));
            } else {
                mStatusView.setText(getResources().getString(R.string.player_2_move));
                b[x][y].setEnabled(false);
                b[x][y].setBackground(getResources().getDrawable(R.drawable.x));
                mTgame.nextMove(3*(x-1)+(y-1));
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.add("New Game");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        refresh();
        return true;
    }


    // Set up the game board.
    private void setBoard() {
        b = new View[4][4];

        b[1][3] = (View) findViewById(R.id.one);
        b[1][2] = (View) findViewById(R.id.two);
        b[1][1] = (View) findViewById(R.id.three);


        b[2][3] = (View) findViewById(R.id.four);
        b[2][2] = (View) findViewById(R.id.five);
        b[2][1] = (View) findViewById(R.id.six);


        b[3][3] = (View) findViewById(R.id.seven);
        b[3][2] = (View) findViewById(R.id.eight);
        b[3][1] = (View) findViewById(R.id.nine);

        // add the click listeners for each button
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                b[i][j].setOnClickListener(new MyClickListener(i, j));
                if (!b[i][j].isEnabled()) {
                    b[i][j].setBackground(null);
                    b[i][j].setEnabled(true);
                }
            }
        }
    }

    public void refresh() {
        mTgame = new TGame(this);
        mBtn_Mvh.setEnabled(true);
        mBtn_HvH.setEnabled(true);
        mStatusView.setText(getResources().getString(R.string.choose_to_start));
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                b[i][j].setOnClickListener(null);
                b[i][j].setBackground(null);
                b[i][j].setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.hvh) {
            mPlayer1 = new HPlayer(this);
            mPlayer2 = new HPlayer(this);
            mTgame.setPlayers(mPlayer1, mPlayer2);
        } else if (view.getId() == R.id.mvh) {
            mPlayer1 = new HPlayer(this);
            mPlayer2 = new MlPlayer(this,mTgame);
            MlPlayer.isTraining = false;
            mTgame.setPlayers(mPlayer1, mPlayer2);
        }
        mStatusView.setText(getResources().getString(R.string.player_1_move));
        setBoard();
        mBtn_Mvh.setEnabled(false);
        mBtn_HvH.setEnabled(false);

    }

    class MyClickListener implements View.OnClickListener {
        int x;
        int y;


        public MyClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }


        public void onClick(View view) {
            if (b[x][y].isEnabled()) {
                if (mTgame.getCurrentPlayer().hashCode() == mPlayer1.hashCode()) {
                    b[x][y].setEnabled(false);
                    b[x][y].setBackground(getResources().getDrawable(R.drawable.o));
                    mTgame.nextMove(3 * (x - 1) + (y - 1));
                } else {
                    b[x][y].setEnabled(false);
                    b[x][y].setBackground(getResources().getDrawable(R.drawable.x));
                    mTgame.nextMove(3 * (x - 1) + (y - 1));
                }
            }
        }
    }
}
