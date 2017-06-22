package com.app.elisoft.guru.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.User;
import com.app.elisoft.guru.TicTacToe.GameManager;
import com.app.elisoft.guru.TicTacToe.Item;
import com.app.elisoft.guru.TicTacToe.Sign;
import com.app.elisoft.guru.Utils.Keys;
import com.app.elisoft.guru.Views.CircleTransform;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import static com.app.elisoft.guru.TicTacToe.Sign.EMPTY;
import static com.app.elisoft.guru.TicTacToe.Sign.O;
import static com.app.elisoft.guru.TicTacToe.Sign.X;

/**
 * Created by eliran.alon on 20-Jun-17.
 */

public class GameSinglePlayerActivity extends BaseActivity {

    private static final String TAG = "GameSinglePlayer";

    private static final int GAME_SIZE = 3;

    Dialog dialog;

    User host_user, com_user, turn;
    Sign myPiece, comPiece;

    int myScore, otherScore, draws;

    GameManager gameManager;

    ImageView[][] matrixView;
    ImageView exit_button;

    ImageView com_icon, host_user_icon, com_sign, host_sign;
    TextView com_name_text, host_user_text;
    TextView turnTitle, score_com, score_host, score_draw;

    public GameSinglePlayerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_player_game);

        gameManager = GameManager.getInstance();
        gameManager.initList();
//        gameManager.initTestList();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleStartGame");
        host_user = (User) bundle.getSerializable("UserHost");
        com_user = (User) bundle.getSerializable("UserClient");

        myScore = 0;
        otherScore = 0;
        draws = 0;

        drawTurn();
        initGameBoardView();
        initGameUI();
        if (turn.getEmail().equals(com_user.getEmail())){
            // Now the com need to play
            comMove();
        }
        updateGameBoard();


    }

    public void drawTurn() {

        if (gameManager.flipCoin() == 0) {
            turn = host_user;
        } else {
            turn = com_user;
        }

        myPiece = Sign.X;
        comPiece = Sign.O;
        host_user.setSign(X);
        com_user.setSign(O);
        gameManager.setMySeed(host_user);
        gameManager.setOppSeed(com_user);

    }

    public void initGameUI(){

        com_sign = (ImageView) findViewById(R.id.game_com_sign_icon);
        host_sign = (ImageView) findViewById(R.id.game_host_sign_icon);

        com_icon = (ImageView) findViewById(R.id.com_icon);
        host_user_icon = (ImageView) findViewById(R.id.invite_host_icon);
        com_name_text = (TextView) findViewById(R.id.com_name);
        host_user_text = (TextView) findViewById(R.id.invite_host_name);

        turnTitle = (TextView) findViewById(R.id.turn_title);
        score_com = (TextView) findViewById(R.id.score_client_title);
        score_host = (TextView) findViewById(R.id.score_host_title);
        score_draw = (TextView) findViewById(R.id.score_title_draw);

        //Setting up user icons
        String client_user_icon_url = com_user.getIconURL();
        if (client_user_icon_url == null) client_user_icon_url = "a";
        Picasso.with(this)
                .load(client_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(com_icon);

        String host_user_icon_url = host_user.getIconURL();
        if (host_user_icon_url == null) host_user_icon_url = "a";
        Picasso.with(this)
                .load(host_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(host_user_icon);

        //Setting up signs
        if (myPiece.equals(X)){
            com_sign.setImageResource(R.drawable.o_icon);
            host_sign.setImageResource(R.drawable.x_icon);
        } else {
            com_sign.setImageResource(R.drawable.x_icon);
            host_sign.setImageResource(R.drawable.o_icon);
        }

        //Setting up user names
        com_name_text.setText(com_user.getEmail().split("@")[0]);
        host_user_text.setText(host_user.getEmail().split("@")[0]);


        exit_button = (ImageView) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateGameRibbonScore();
        updateRibbonTurn();
    }

    public void updateGameRibbonScore(){
        String drawString;
        drawString = " Draw's: " + String.valueOf(draws);
        score_com.setText(String.valueOf(otherScore));
        score_host.setText(String.valueOf(myScore));
        score_draw.setText(drawString);
    }

    public void updateRibbonTurn(){
        String turnS = "It's " + turn.getEmail().split("@")[0] + " turn";
        turnTitle.setText(turnS);
    }

    public void initGameBoardView() {
        matrixView = new ImageView[3][3];
        matrixView[0][0] = (ImageView) findViewById(R.id.one);
        matrixView[0][1] = (ImageView) findViewById(R.id.two);
        matrixView[0][2] = (ImageView) findViewById(R.id.three);
        matrixView[1][0] = (ImageView) findViewById(R.id.four);
        matrixView[1][1] = (ImageView) findViewById(R.id.five);
        matrixView[1][2] = (ImageView) findViewById(R.id.six);
        matrixView[2][0] = (ImageView) findViewById(R.id.seven);
        matrixView[2][1] = (ImageView) findViewById(R.id.eight);
        matrixView[2][2] = (ImageView) findViewById(R.id.nine);

        for(int i =0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                matrixView[i][j].setVisibility(View.VISIBLE);
                matrixView[i][j].setImageResource(R.drawable.empty_icon);
                //Todo: Click listener hare
                matrixView[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getPosition(view);
                        Log.d(TAG, "I clicked on item: " + position);
                        if (isMyTurn()) {
                            Log.d(TAG, "Its my turn...");
                            if (gameManager.canClick(position)) {
                                Log.d(TAG, "The user click on empty tile");
                                gameManager.setMove(position, myPiece);
                                //update the new layout
                                updateGameBoard();
                                if (gameManager.checkWin()) {
                                    //ToDo: there is a winner here :)
                                    Log.d(TAG,"there is a winner here :)");

                                    showGameResultDialog(Keys.GAME_WIN);
                                    addPoints(host_user.getEmail());
                                    updateGameRibbonScore();
                                    turn = new User();
                                }
                                else
                                {
                                    if (gameManager.getMoveNumber() == 9 ) {
                                        //ToDo: There is a draw here
                                        Log.d(TAG,"There is a draw here");
                                        showGameResultDialog(Keys.GAME_DRAW);
                                        addPoints("draw");
                                        updateGameRibbonScore();
                                        turn = new User();
                                    }
                                    else
                                    {
                                        // just normal move...
                                        Log.d(TAG,"just normal move...");
                                        //ToDo: send move to other
                                        changeTurn();
                                        comMove();
                                    }
                                }
                                // Now the com need to play


                            }

                        }
                        else {
                            Log.d(TAG, "Its not my turn...");
                        }

                    }
                });

            }
        }

    }

    private void comMove(){
        Log.d(TAG,"Starting computer move - comMove()");
        int move = gameManager.calMove(com_user);
        gameManager.setMove(move, com_user.getSign());

        changeTurn();
        updateGameBoard();
    }

    private void addPoints(String to) {
        if (to.equals("draw")) {
            draws++;
        } else {
            if (to.equals(host_user.getEmail())) {
                myScore++;
            } else otherScore++;
        }
    }

    public void changeTurn(){
        turn = getOtherPlayer();
        Log.d(TAG, "Turn change to " + turn.getEmail());
        updateRibbonTurn();
    }

    public User getOtherPlayer(){
        if (turn.getEmail().equals(host_user.getEmail())) return com_user;
        else return host_user;
    }

    public void showGameResultDialog(String message) {
        dialog = new Dialog(this, R.style.FullHeightDialog);

        dialog.setContentView(R.layout.result_dialog);

        ImageView dialogImage = (ImageView) dialog.findViewById(R.id.result_dialog);
        Button dialogButton = (Button) dialog.findViewById(R.id.result_dialog_again);

        if (message.equals(Keys.GAME_WIN)) {
            dialogImage.setImageResource(R.drawable.win_icon);
        } else if (message.equals(Keys.GAME_LOSE)) {
            dialogImage.setImageResource(R.drawable.loser_icon);
        } else if (message.equals(Keys.GAME_DRAW)) {
            dialogImage.setImageResource(R.drawable.draw_icon);
        }
        YoYo.with(Techniques.BounceIn)
                .duration(1000)
                .playOn(dialogImage);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetRound();
//                sendMessage(Keys.MESSAGE_NEW_GAME, turn.getEmail());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void resetRound() {
        gameManager.initList();
        updateGameBoard();
        if (gameManager.flipCoin() == 0) {
            turn = host_user;
        } else turn = com_user;
        Log.d(TAG, "Its turn: "+ turn.getEmail());
        initGameUI();
    }

    public boolean isMyTurn(){
        if (turn.getEmail().equals(host_user.getEmail())){
            //Its my turn :)
            return true;
        } else return false;
    }

    public void updateGameBoard(){
        Item[][] data = gameManager.getMatrix();

        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (data[i][j].getState() == EMPTY) {
                    matrixView[i][j].setImageResource(R.drawable.empty_icon);
                } else {
                    if (data[i][j].getState() == Sign.X) {
                        matrixView[i][j].setImageResource(R.drawable.x_icon);

                    } else {
                        if (data[i][j].getState() == Sign.O) {
                            matrixView[i][j].setImageResource(R.drawable.o_icon);
                        }
                    }
                }
            }
        }

    }

    public int getPosition(View view){
        int id = view.getId();
        switch (id) {
            case R.id.one: {
                return 1;
            }
            case R.id.two: {
                return 2;
            }
            case R.id.three: {
                return 3;
            }
            case R.id.four: {
                return 4;
            }
            case R.id.five: {
                return 5;
            }
            case R.id.six: {
                return 6;
            }
            case R.id.seven: {
                return 7;
            }
            case R.id.eight: {
                return 8;
            }
            case R.id.nine: {
                return 9;
            }
            default:
                return 1;
        }
    }

}
