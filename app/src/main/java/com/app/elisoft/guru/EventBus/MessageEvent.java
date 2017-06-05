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

    public static class OnAcceptInvite extends MessageEvent {
        String message;
        public OnAcceptInvite(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class UserArrive extends MessageEvent {
        String message;
        public UserArrive(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class MoveRequest extends MessageEvent {
        String message;
        public MoveRequest(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class LastMoveRequestWin extends MessageEvent {
        String message;
        public LastMoveRequestWin(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
    public static class LastMoveRequestDraw extends MessageEvent {
        String message;
        public LastMoveRequestDraw(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
    public static class NewGameRequest extends MessageEvent {
        String message;
        public NewGameRequest(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
    public static class QuitRequest extends MessageEvent {
        String message;
        public QuitRequest(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
