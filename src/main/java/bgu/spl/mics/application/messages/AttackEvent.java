package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private List<Integer> ewoksSerials;
    private int duration;

	public AttackEvent(List<Integer> serials, int duration){
	    ewoksSerials = serials;

	    this.duration = duration;
    }

    public List<Integer> getEwoksSerials(){
	    return ewoksSerials;
    }

    public int getDuration(){
	    return duration;
    }
}
