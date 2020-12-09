package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class DummyBroadcast implements Broadcast {
    //------------------------------------fields----------------------------------------------
    String message;
    //----------------------------------constructors------------------------------------------
    public DummyBroadcast(String message) {
        this.message = message;
    }
}
