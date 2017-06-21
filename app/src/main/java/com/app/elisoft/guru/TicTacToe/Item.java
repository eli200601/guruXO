package com.app.elisoft.guru.TicTacToe;


public class Item {

    int position;
    Sign state;

    public Item(int position, Sign state) {
        this.position = position;
        this.state = state;
    }

    public Item() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Sign getState() {
        return state;
    }

    public void setState(Sign state) {
        this.state = state;
    }


}
