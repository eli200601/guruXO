package com.app.elisoft.guru.TicTacToe;


import android.content.ClipData;
import android.widget.ImageView;

import java.util.Random;

import static com.app.elisoft.guru.TicTacToe.Sign.EMPTY;

public class GameManager {

    private static GameManager instance;

    private static final int GAME_SIZE = 3;
    private int moveNumber;
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
        moveNumber = 0;
        matrix = new Item[GAME_SIZE][GAME_SIZE];
        int position = 1;
        for (int i=0; i<GAME_SIZE; i++){
            for (int j=0; j<GAME_SIZE; j++){
                Item item = new Item(position, EMPTY);
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

    public boolean chechWin(){
        if (moveNumber<4) return false;
        else {
                 if ( matrix[0][0].getState() == matrix[0][1].getState() && matrix[0][1].getState() == matrix[0][2].getState() && (matrix[0][2].getState() == Sign.X || matrix[0][2].getState() == Sign.O)) return true;
            else if ( matrix[1][0].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[1][2].getState() && (matrix[1][2].getState() == Sign.X || matrix[1][2].getState() == Sign.O)) return true;
            else if ( matrix[2][0].getState() == matrix[2][1].getState() && matrix[2][1].getState() == matrix[2][2].getState() && (matrix[2][2].getState() == Sign.X || matrix[2][2].getState() == Sign.O)) return true;
            else if ( matrix[0][0].getState() == matrix[1][0].getState() && matrix[1][0].getState() == matrix[2][0].getState() && (matrix[2][0].getState() == Sign.X || matrix[2][0].getState() == Sign.O)) return true;
            else if ( matrix[0][1].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[2][1].getState() && (matrix[2][1].getState() == Sign.X || matrix[2][1].getState() == Sign.O)) return true;
            else if ( matrix[0][2].getState() == matrix[1][2].getState() && matrix[1][2].getState() == matrix[2][2].getState() && (matrix[2][2].getState() == Sign.X || matrix[2][2].getState() == Sign.O)) return true;
            else if ( matrix[0][0].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[2][2].getState() && (matrix[2][2].getState() == Sign.X || matrix[2][2].getState() == Sign.O)) return true;
            else if ( matrix[0][2].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[2][0].getState() && (matrix[2][0].getState() == Sign.X || matrix[2][0].getState() == Sign.O)) return true;
        }
        return false;
    }


    public boolean canClick(int position) {
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (matrix[i][j].getPosition() == position) {
                    return matrix[i][j].getState() == EMPTY && (moveNumber < GAME_SIZE*GAME_SIZE);
                }
            }
        }
        return false;
    }

    public void setMove(int position, Sign type){
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (matrix[i][j].getPosition() == position) {
                    matrix[i][j].setState(type);
                }
            }
        }
        moveNumber++;
    }

    public Item[][] getMatrix(){
        return matrix;
    }

    public int getMoveNumber(){
        return moveNumber;
    }

}
