package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;
/**
 * The {@link AttackEvent class is the implementation of the Event interface.
 */
public class AttackEvent implements Event<Boolean> {
//----------------------------------fields----------------------------------

    private List<Integer> ewoksSerials;
    private int duration;
//-------------------------------constructors-------------------------------
    /**
     * @param serials is the ewoks required for this attack - initialized to field ewoksSerials
     * @param duration is the time takes to complete this attack - initialized to field duration
     */
    public AttackEvent(List<Integer> serials, int duration) {
        ewoksSerials = serials;
        this.duration = duration;
    }
//----------------------------------getters---------------------------------
    /**
     * @return list of ewoks required for the attack
     */
    public List<Integer> getEwoksSerials() {
        return ewoksSerials;
    }
    /**
     * @return time that required for the attack
     */
    public int getDuration() {
        return duration;
    }
}
