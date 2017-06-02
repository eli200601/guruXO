package com.app.elisoft.guru.TicTacToe;


import android.content.ClipData;
import android.widget.ImageView;

import java.util.Random;

public class GameManager {

    private static GameManager instance;

    private static final int GAME_SIZE = 3;
    Item[][] matrix;

    public static GameManager getInstance(){
        if (instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        initList();

    }

    public void initList() {
        matrix = new Item[GAME_SIZE][GAME_SIZE];
        int position = 1;
        for (int i=0; i<GAME_SIZE; i++){
            for (int j=0; j<GAME_SIZE; j++){
                Item item = new Item(position, Sign.EMPTY);
                matrix[i][j] = item;
                position++;
            }
        }
    }

    public int flipCoin(){
        Random r = new Random();
        int i1 = r.nextInt(2);
        return i1;
    }


}
