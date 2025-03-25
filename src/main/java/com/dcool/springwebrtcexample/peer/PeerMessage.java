package com.dcool.springwebrtcexample.peer;

import lombok.Data;

public @Data class PeerMessage {
    private String key;
    private String roomId;

    public PeerMessage(){
    }
    public PeerMessage(String key){
        this.key = key;
    }
}
