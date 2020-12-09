package bgu.spl.mics.application.services;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
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
//------------------------------------fields----------------------------------------------
    private long duration;
    private Diary diary;
//----------------------------------constructors------------------------------------------
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
        diary = Diary.getInstance();

    }
//------------------------------------methods---------------------------------------------
    @Override
    protected void initialize() {
        // DeactivationEvent
        Callback<DeactivationEvent> DeCallback = (DeactivationEvent e) -> {
            try {
                MILLISECONDS.sleep(duration);
            } catch (InterruptedException eX) {
                eX.printStackTrace();
            }
            complete(e, Boolean.TRUE);
            diary.setDeactivateTime(System.currentTimeMillis()); // update log in diary
        };
        subscribeEvent(DeactivationEvent.class, DeCallback);

        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback -> {
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });
    }
}

