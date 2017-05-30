package com.app.elisoft.guru.EventBus;


public class MessageEvent {

    public static class OnInviteToPlay extends MessageEvent {
        String hostName;
        String host_id;
        String game_room;
        public OnInviteToPlay(String hostName, String host_id, String game_room) {
            this.hostName = hostName;
            this.host_id = host_id;
            this.game_room = game_room;
        }
        public String getHostName() {
            return hostName;
        }
        public String getHostId() {
            return host_id;
        }
        public String getGameRoom() {
            return game_room;
        }
    }


}