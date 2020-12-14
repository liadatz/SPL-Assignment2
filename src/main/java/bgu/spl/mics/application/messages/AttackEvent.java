package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AttackEvent implements Event<Boolean> {
//----------------------------------fields----------------------------------
    private List<Integer> ewoksSerials;
    private int duration;
//-------------------------------constructors-------------------------------
    public AttackEvent(List<Integer> serials, int duration) {
        ewoksSerials = serials;
        this.duration = duration;
    }
//----------------------------------getters---------------------------------
    public List<Integer> getEwoksSerials() {
        return ewoksSerials;
    }
    public int getDuration() {
        return duration;
    }
}
