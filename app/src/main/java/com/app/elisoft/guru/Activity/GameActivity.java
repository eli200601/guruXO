package com.app.elisoft.guru.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.elisoft.guru.EventBus.MessageEvent;
import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Services.SendMessageToDevice;
import com.app.elisoft.guru.Table.User;
import com.app.elisoft.guru.TicTacToe.GameManager;
import com.app.elisoft.guru.TicTacToe.Item;
import com.app.elisoft.guru.TicTacToe.Sign;
import com.app.elisoft.guru.Utils.Keys;
import com.app.elisoft.guru.Views.CircleTransform;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.app.elisoft.guru.TicTacToe.Sign.EMPTY;
import static com.app.elisoft.guru.TicTacToe.Sign.X;


public class GameActivity extends BaseActivity {

    private static final String TAG = GameActivity.class.getSimpleName();
    private static final int GAME_SIZE = 3;

    //    Player1      Player2   profile = me
    User host_user, client_user, profile, turn;
    Sign myPiece, otherPiece;

    Dialog dialog;
    AlertDialog exitScreen;

    String game_room;
    ImageView[][] matrixView;
    TextView turnTitle, score_client, score_host, score_draw;
    ImageView exit_button;

    ImageView client_user_icon, host_user_icon, client_sign, host_sign;
    TextView client_user_text, host_user_text;

    int myScore, otherScore, draws;

    GameManager gameManager;

    private EventBus eventBus = EventBus.getDefault();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        gameManager = GameManager.getInstance();
        gameManager.initList();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundleStartGame");
        client_user = (User) bundle.getSerializable("UserClient");
        host_user = (User) bundle.getSerializable("UserHost");
        game_room = bundle.getString("game_room");
        profile = (User) bundle.getSerializable("profile");
        turn = new User();

        myScore = 0;
        otherScore = 0;
        draws = 0;

        initGameBoard();

        exit_button = (ImageView) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(Keys.MESSAGE_QUIT, profile.getEmail());
                finish();
            }
        });

        if (host_user.getEmail().equals(profile.getEmail())) {
            //This is Host device
            if (gameManager.flipCoin() == 0) {
                turn = host_user;
            } else turn = client_user;
            Log.d(TAG, "Its turn: "+ turn.getEmail());
            myPiece = Sign.X;
            otherPiece = Sign.O;
            sendMessage(Keys.MESSAGE_ARRIVE, turn.getEmail());
            initRibbon();
//            sendMessage(Keys.MESSAGE_ARRIVE, turn.getEmail());
        } else {
            myPiece = Sign.O;
            otherPiece = Sign.X;
            //This is Client device
            showProgressDialog("Waiting for Host to join");
        }

    }

    private void initRibbon() {

//        title = (TextView) findViewById(R.id.status_title);
//        desc = (TextView) findViewById(R.id.sub_title);
        client_sign = (ImageView) findViewById(R.id.game_client_sign_icon);
        host_sign = (ImageView) findViewById(R.id.game_host_sign_icon);

        client_user_icon = (ImageView) findViewById(R.id.invite_client_icon);
        host_user_icon = (ImageView) findViewById(R.id.invite_host_icon);
        client_user_text = (TextView) findViewById(R.id.invite_client_name);
        host_user_text = (TextView) findViewById(R.id.invite_host_name);

        turnTitle = (TextView) findViewById(R.id.turn_title);
        score_client = (TextView) findViewById(R.id.score_client_title);
        score_host = (TextView) findViewById(R.id.score_host_title);
        score_draw = (TextView) findViewById(R.id.score_title_draw);

        //Setting up user icons
        String client_user_icon_url = client_user.getIconURL();
        if (client_user_icon_url == null) client_user_icon_url = "a";
        Picasso.with(this)
                .load(client_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(client_user_icon);

        String host_user_icon_url = host_user.getIconURL();
        if (host_user_icon_url == null) host_user_icon_url = "a";
        Picasso.with(this)
                .load(host_user_icon_url)
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(host_user_icon);

        //Setting up signs
        if (profile.getEmail().equals(client_user.getEmail())) {
            Sign my_Piece = myPiece;
            if (my_Piece.equals(X)){
                client_sign.setImageResource(R.drawable.x_icon);
                host_sign.setImageResource(R.drawable.o_icon);
            } else {
                client_sign.setImageResource(R.drawable.o_icon);
                host_sign.setImageResource(R.drawable.x_icon);
            }
        }
        else {
            Sign my_Piece = myPiece;
            if (my_Piece.equals(X)){
                client_sign.setImageResource(R.drawable.o_icon);
                host_sign.setImageResource(R.drawable.x_icon);
            } else {
                client_sign.setImageResource(R.drawable.x_icon);
                host_sign.setImageResource(R.drawable.o_icon);
            }
        }

        //Setting up user names
        client_user_text.setText(client_user.getEmail().split("@")[0]);
        host_user_text.setText(host_user.getEmail().split("@")[0]);

        //Setting up title
//        String otherName;
//        if (profile.getEmail().equals(client_user.getEmail())) otherName = host_user.getEmail().split("@")[0];
//            else otherName = client_user.getEmail().split("@")[0];
//        String titleS = profile.getEmail().split("@")[0] + " Vs " + otherName;
//        title.setText(titleS);

        //Setting up description
//        String descS = "My Sign is: " + myPiece.toString();
//        desc.setText(descS);

        //Setting up Score
        updateRibbonScore();

        //Setting up Turn text
        String turnS = "It's " + turn.getEmail().split("@")[0] + " turn";
        turnTitle.setText(turnS);

    }

    public void sendMessage(String type, String message) {
        Intent intentNew = new Intent(GameActivity.this, SendMessageToDevice.class);
        //host_name is sender
        intentNew.putExtra("host_name", profile.getEmail());
        intentNew.putExtra("host_uid", host_user.getUid());
        intentNew.putExtra("address_prefix", "room_");
        intentNew.putExtra("client_uid", game_room);
        intentNew.putExtra("game_room", game_room);
        intentNew.putExtra("request_type", type);
        intentNew.putExtra("message", message);
        intentNew.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startService(intentNew);
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
                sendMessage(Keys.MESSAGE_NEW_GAME, turn.getEmail());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showExitDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        finish();
                    }
                });
        exitScreen = builder.create();
        exitScreen.show();

    }


    public void initGameBoard(){
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


        int position = 1;
        for(int i =0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                matrixView[i][j].setVisibility(View.VISIBLE);
                matrixView[i][j].setImageResource(R.drawable.empty_icon);
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
                                if (gameManager.chechWin()) {
                                    //ToDo: there is a winner here :)
                                    Log.d(TAG,"there is a winner here :)");

                                    sendMessage(Keys.MESSAGE_LAST_MOVE_WIN, String.valueOf(position));
                                    showGameResultDialog(Keys.GAME_WIN);
                                    addPoints(profile.getEmail());
                                    updateRibbonScore();
                                    turn = new User();
                                }
                                else
                                {
                                    if (gameManager.getMoveNumber() == 9 ) {
                                        //ToDo: There is a draw here
                                        Log.d(TAG,"There is a draw here");
                                        sendMessage(Keys.MESSAGE_LAST_MOVE_DRAW, String.valueOf(position));
                                        showGameResultDialog(Keys.GAME_DRAW);
                                        addPoints("draw");
                                        updateRibbonScore();
                                        turn = new User();
                                    }
                                    else
                                    {
                                        // just normal move...
                                        Log.d(TAG,"just normal move...");
                                        //ToDo: send move to other
                                        sendMessage(Keys.MESSAGE_MOVE, String.valueOf(position));
                                        // change turn
                                        changeTurn();
                                    }
                                }
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

    public void resetRound() {
        gameManager.initList();
        updateGameBoard();
        if (gameManager.flipCoin() == 0) {
            turn = host_user;
        } else turn = client_user;
        Log.d(TAG, "Its turn: "+ turn.getEmail());
        initRibbon();
    }

    public void resetRoundWithoutCoin() {
        gameManager.initList();
        updateGameBoard();
        Log.d(TAG, "Its turn: "+ turn.getEmail());
        initRibbon();
    }

    private void addPoints(String to) {
        if (to.equals("draw")) {
            draws++;
        } else {
            if (to.equals(profile.getEmail())) {
                myScore++;
            } else otherScore++;
        }
    }

    public void changeTurn(){
        turn = getOtherPlayer();
        updateRibbon();
    }

    public void updateRibbon(){
        String turnS = "It's " + turn.getEmail().split("@")[0] + " turn";
        turnTitle.setText(turnS);
    }

    public void updateRibbonScore(){
        String sep = " : ";
        String scoreClient, scoreHost, drawString;
//        scoreString = String.valueOf(myScore) + sep + String.valueOf(otherScore);
        drawString = " Draw's: " + String.valueOf(draws);
//        score.setText(scoreString);
        if (profile.getEmail().equals(client_user.getEmail())) {
            //i am client
            score_client.setText(String.valueOf(myScore));
            score_host.setText(String.valueOf(otherScore));
        }
        else {
            //i am host
            score_client.setText(String.valueOf(otherScore));
            score_host.setText(String.valueOf(myScore));

        }
        score_draw.setText(drawString);
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


    public boolean isMyTurn(){
        if (turn.getEmail().equals(profile.getEmail())){
            //Its my turn :)
            return true;
        } else return false;
    }



    @Override
    protected void onResume() {
        super.onResume();
        FirebaseMessaging.getInstance().subscribeToTopic("room_" + game_room);
        eventBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("room_" + game_room);
        eventBus.unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendMessage(Keys.MESSAGE_QUIT, profile.getEmail());
        updateUserPoints();

    }

    public void updateUserPoints() {
        Log.d(TAG, "Sending user progress");
        int myWins, myLoses, myDraws;
        Log.d(TAG, "previous wins: " + profile.getMyWins());

        if (myScore+otherScore+draws > 0) {
            if (profile.getMyWins() == null ) myWins = myScore;
            else myWins = Integer.valueOf(profile.getMyWins()) + myScore;

            Log.d(TAG, "previous Loses: " + profile.getMyLoses());
            if (profile.getMyLoses() == null ) myLoses = otherScore;
            else myLoses = Integer.valueOf(profile.getMyLoses()) + otherScore;

            Log.d(TAG, "previous Draws: " + profile.getMyDraws());
            if (profile.getMyDraws() == null ) myDraws = draws;
            else myDraws = Integer.valueOf(profile.getMyDraws()) + draws;

            mDatabase.child("users").child(profile.getUid()).child("myWins").setValue(String.valueOf(myWins));
            mDatabase.child("users").child(profile.getUid()).child("myLoses").setValue(String.valueOf(myLoses));
            mDatabase.child("users").child(profile.getUid()).child("myDraws").setValue(String.valueOf(myDraws));
        }





    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        Log.d(TAG, "message sender: " + messageEvent.getSender());
        if (messageEvent.getSender().equals(profile.getEmail())) {
            Log.d(TAG, "This message is not for me !!!");
            return;
        }
        if (messageEvent instanceof MessageEvent.UserArrive) {
            //Host arrived (This is client  device code)
//            String sender = (MessageEvent.UserArrive) messageEvent).getMessage();
            Log.d(TAG, "Host arrived (This is client  device code) | its turn: " + ((MessageEvent.UserArrive) messageEvent).getMessage());
            String turnString = ((MessageEvent.UserArrive) messageEvent).getMessage();
            if (turnString.equals(host_user.getEmail())) {
                turn = host_user;
            } else turn = client_user;

            initRibbon();
            hideProgressDialog();
        } else {
            if (messageEvent instanceof MessageEvent.MoveRequest) {
                int move = Integer.valueOf( ((MessageEvent.MoveRequest) messageEvent).getMessage() );
                if (gameManager.canClick(move)) {
                    Log.d(TAG, "This is a move from the other player");
                    gameManager.setMove(move, otherPiece);
                    updateGameBoard();
                    changeTurn();
                    updateRibbon();
                }

            } else {
                if (messageEvent instanceof MessageEvent.LastMoveRequestWin) {
                    int move = Integer.valueOf( ((MessageEvent.LastMoveRequestWin) messageEvent).getMessage() );
                    if (gameManager.canClick(move)) {
                        Log.d(TAG, "This is a win move from the other player");
                        gameManager.setMove(move, otherPiece);
                        otherScore++;
                        updateGameBoard();
                        updateRibbonScore();
                        turn = new User();
                        showGameResultDialog(Keys.GAME_LOSE);
                    }
                } else {
                    if (messageEvent instanceof MessageEvent.LastMoveRequestDraw) {
                        //when draw
                        int move = Integer.valueOf( ((MessageEvent.LastMoveRequestDraw) messageEvent).getMessage() );
                        if (gameManager.canClick(move)) {
                            gameManager.setMove(move, otherPiece);
                            draws++;
                            updateRibbonScore();
                            updateGameBoard();
                            turn = new User();
                            showGameResultDialog(Keys.GAME_DRAW);
                        }
                    } else {
                        if (messageEvent instanceof MessageEvent.NewGameRequest) {
                            Log.d(TAG, "Got new game request");
                            String turnString = ((MessageEvent.NewGameRequest) messageEvent).getMessage();
                            if (turnString.equals(host_user.getEmail())) {
                                turn = host_user;
                            } else turn = client_user;
                            initRibbon();
                            dialog.dismiss();
                            resetRoundWithoutCoin();
                        } else {
                            if (messageEvent instanceof MessageEvent.QuitRequest) {
                                Log.d(TAG, "Opponent has left the room");
                                showExitDialog("Opponent has left the room");
                            }
                        }
                    }
                }
            }
        }
    }

    public User getOtherPlayer(){
        if (turn.getEmail().equals(host_user.getEmail())) return client_user;
        else return host_user;
//        if (profile.getEmail().equals(host_user.getEmail())) {
//            return client_user;
//        } else {
//            return host_user;
//        }
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
