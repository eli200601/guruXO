package com.app.elisoft.guru.Table;


import java.io.Serializable;

public class GameRoom implements Serializable{

    private String room_id;
    private User host, client;

    public GameRoom() {}

    public GameRoom(String room_name, User host, User client) {
        this.room_id = room_name;
        this.host = host;
        this.client = client;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
}
