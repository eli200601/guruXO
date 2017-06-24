package com.app.elisoft.guru.EventBus;


public class MessageEvent {

    String sender;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

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

    public static class OnUserClickInLobby extends MessageEvent {
        int position;
        public OnUserClickInLobby(int position) {
            this.position = position;
        }
        public int getPosition() {
            return position;
        }
    }


    /*--------------------------------- Game Requests ---------------------------------*/
    public static class OnAcceptInvite extends MessageEvent {
        String message;
        public OnAcceptInvite(String message) {
            this.message = message;

        }
        public String getMessage() {
            return message;
        }
    }
    public static class OnDeclineInvite extends MessageEvent {
        String message;
        public OnDeclineInvite(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }


    public static class UserArrive extends MessageEvent {
        String message;
        public UserArrive(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class MoveRequest extends MessageEvent {
        String message;
        public MoveRequest(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }
        public String getMessage() {
            return message;
        }
    }

    public static class LastMoveRequestWin extends MessageEvent {
        String message;
        public LastMoveRequestWin(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }
        public String getMessage() {
            return message;
        }
    }
    public static class LastMoveRequestDraw extends MessageEvent {
        String message;
        public LastMoveRequestDraw(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }
        public String getMessage() {
            return message;
        }
    }
    public static class NewGameRequest extends MessageEvent {
        String message;
        public NewGameRequest(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }
        public String getMessage() {
            return message;
        }
    }
    public static class QuitRequest extends MessageEvent {
        String message;
        public QuitRequest(String message, String sender) {
            this.message = message;
            this.sender = sender;
        }
        public String getMessage() {
            return message;
        }
    }

    /*--------------------------------- Single Game Events ---------------------------------*/

    public static class OnFindingBestMove extends MessageEvent {
        int pos;
        public OnFindingBestMove(int pos) {
            this.pos = pos;
        }
        public int getPosition() {
            return pos;
        }
    }
}
