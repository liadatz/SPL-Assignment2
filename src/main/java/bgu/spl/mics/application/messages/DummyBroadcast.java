package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
/**
 * The {@link DummyBroadcast class is the implementation of the Broadcast interface.
 * this class is used for testing
 */
public class DummyBroadcast implements Broadcast {
    //------------------------------------fields----------------------------------------------
    String message;
    //----------------------------------constructors------------------------------------------
    public DummyBroadcast(String message) {
        this.message = message;
    }
}
