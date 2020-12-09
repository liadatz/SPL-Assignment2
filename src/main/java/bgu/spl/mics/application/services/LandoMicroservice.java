package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;
    Diary diary;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback->{
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });

        // BombDestroyerEvent
        Callback<BombDestroyerEvent> DeCallback = (BombDestroyerEvent e)->{
            try {
                MILLISECONDS.sleep(duration);
            }
            catch (InterruptedException eX){
                eX.printStackTrace();
            }
            TerminateBroadcast terminate = new TerminateBroadcast();
            sendBroadcast(terminate); //notify all microservices that the attack was done
        };
        subscribeEvent(BombDestroyerEvent.class, DeCallback);
    }
}
