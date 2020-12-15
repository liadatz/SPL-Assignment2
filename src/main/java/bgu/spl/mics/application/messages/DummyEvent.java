package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
/**
 * The {@link DummyEvent class is the implementation of the Event interface.
 * this class is used for testing
 */
public class DummyEvent implements Event<Boolean> {
//------------------------------------fields----------------------------------------------
    String message;
//----------------------------------constructors------------------------------------------
    public DummyEvent(String message) {
        this.message = message;
    }
}
