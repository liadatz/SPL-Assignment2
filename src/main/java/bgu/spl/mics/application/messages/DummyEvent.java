package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DummyEvent implements Event<Boolean> {
//------------------------------------fields----------------------------------------------
    String message;
//----------------------------------constructors------------------------------------------
    public DummyEvent(String message) {
        this.message = message;
    }
}
