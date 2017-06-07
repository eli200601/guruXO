package com.app.elisoft.guru.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.elisoft.guru.R;
import com.app.elisoft.guru.Table.User;
import com.app.elisoft.guru.Utils.Keys;
import com.app.elisoft.guru.Views.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LeaderBoardActivity extends BaseActivity {
    private static final String TAG = "LeaderBoardActivity";
    private ImageView exit_button;
    private ArrayList<User> topThree;

    TextView one_name, one_win, one_lose, one_draw;
    TextView two_name, two_win, two_lose, two_draw;
    TextView three_name, three_win, three_lose, three_draw;

    ImageView one_icon, two_icon, three_icon;

    User one;
    User two;
    User three;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Intent intent = getIntent();
        if (intent != null) {
            topThree = (ArrayList<User>) intent.getSerializableExtra("top_three_list");
            for (User user: topThree){
                Log.d(TAG, "Top 3: " + user.getEmail());
            }
        }

        one = topThree.get(0);
        two = topThree.get(1);
        three = topThree.get(2);

        initView();

        one_name.setText(one.getEmail().split("@")[0]);
        one_win.setText("Wins: " + one.getMyWins());
        one_lose.setText("Loses: " + one.getMyLoses());
        one_draw.setText("Draws: " + one.getMyDraws());

        two_name.setText(two.getEmail().split("@")[0]);
        two_win.setText("Wins: " + two.getMyWins());
        two_lose.setText("Loses: " + two.getMyLoses());
        two_draw.setText("Draws: " + two.getMyDraws());

        three_name.setText(three.getEmail().split("@")[0]);
        three_win.setText("Wins: " + three.getMyWins());
        three_lose.setText("Loses: " + three.getMyLoses());
        three_draw.setText("Draws: " + three.getMyDraws());

        Picasso.with(this)
                .load(one.getIconURL())
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(one_icon);

        Picasso.with(this)
                .load(two.getIconURL())
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(two_icon);

        Picasso.with(this)
                .load(three.getIconURL())
                .placeholder(R.mipmap.profile_icon)
                .error(R.mipmap.profile_icon)
                .transform(new CircleTransform())
                .into(three_icon);

        exit_button.setOnClickListener(exit);



    }

    public void initView() {

        exit_button = (ImageView) findViewById(R.id.exit_button);

        one_icon = (ImageView) findViewById(R.id.icon_user_first_place);
        two_icon = (ImageView) findViewById(R.id.icon_user_second_place);
        three_icon = (ImageView) findViewById(R.id.icon_user_third_place);

        one_name = (TextView) findViewById(R.id.user_name_first_place);
        one_win = (TextView) findViewById(R.id.first_place_win_status);
        one_lose = (TextView) findViewById(R.id.first_place_lose_status);
        one_draw = (TextView) findViewById(R.id.first_place_draw_status);

        two_name = (TextView) findViewById(R.id.user_name_second_place);
        two_win = (TextView) findViewById(R.id.second_place_win_status);
        two_lose = (TextView) findViewById(R.id.second_place_lose_status);
        two_draw = (TextView) findViewById(R.id.second_place_draw_status);

        three_name = (TextView) findViewById(R.id.user_name_third_place);
        three_win = (TextView) findViewById(R.id.third_place_win_status);
        three_lose = (TextView) findViewById(R.id.third_place_lose_status);
        three_draw = (TextView) findViewById(R.id.third_place_draw_status);
    }

    private View.OnClickListener exit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };



}
