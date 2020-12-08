package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        Callback<BombDestroyerEvent> DeCallback = (BombDestroyerEvent e)->{
            try {
                MILLISECONDS.sleep(duration);
            }
            catch (InterruptedException eX){
                eX.printStackTrace();
            }
            TerminateBroadcast terminate = new TerminateBroadcast();
            subscribeBroadcast(TerminateBroadcast.class, callback->terminate()); //should get the terminateBroadcast? or just terminate alone?
            sendBroadcast(terminate); //notify all microservices that the attack was done
            //terminate(); // replaces line 34?
        };
       
    }
}
