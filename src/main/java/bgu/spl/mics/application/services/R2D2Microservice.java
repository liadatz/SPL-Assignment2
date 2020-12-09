package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import sun.font.TrueTypeFont;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;
    private Diary diary;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
        diary = Diary.getInstance();

    }

    @Override
    protected void initialize() {
        Callback <DeactivationEvent> DeCallback = (DeactivationEvent e)->{
            try {
                MILLISECONDS.sleep(duration);
            }
            catch (InterruptedException eX){
                eX.printStackTrace();
            }
            complete(e, Boolean.TRUE);
            diary.setDeactivateTime(System.currentTimeMillis()); // update log in diary
            BombDestroyerEvent bombEvent = new BombDestroyerEvent();
            sendEvent(bombEvent); // notify Lando that shield deactivation is done
        };
        subscribeEvent(DeactivationEvent.class, DeCallback);
        subscribeBroadcast(TerminateBroadcast.class, callback->terminate());
    }
}
