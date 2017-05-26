package com.app.elisoft.guru.EventBus;


public class MessageEvent {

    public static class OnInviteToPlay extends MessageEvent {
        String hostName;
        String host_id;
        public OnInviteToPlay(String hostName, String host_id) {
            this.hostName = hostName;
            this.host_id = host_id;
        }
        public String getHostName() {
            return hostName;
        }
        public String getHost_id() {
            return host_id;
        }
    }


}
