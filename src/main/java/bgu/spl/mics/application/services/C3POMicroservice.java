package bgu.spl.mics.application.services;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishAttacksBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    //------------------------------------fields----------------------------------------------
    private Diary diary;
    private Ewoks ewoks;
    //----------------------------------constructors------------------------------------------
    public C3POMicroservice() {
        super("C3PO");
        diary = Diary.getInstance();
        ewoks = Ewoks.getInstance();
    }
    //------------------------------------methods---------------------------------------------
    @Override
    protected void initialize() {
        // Attacks
        Callback<AttackEvent> attackCallback = (AttackEvent e) -> {
            ewoks.acquireEwoks(e.getEwoksSerials()); //blocking method
            try {
                MILLISECONDS.sleep(e.getDuration());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            ewoks.releaseEwoks(e.getEwoksSerials());
            complete(e, true);
            diary.increaseTotalAttacks();
        };
        subscribeEvent(AttackEvent.class, attackCallback);
        diary.increaseNumOfAttackers();

        // FinishAttacks
        subscribeBroadcast(FinishAttacksBroadcast.class, callback -> {
            diary.setFinishTime(this, System.currentTimeMillis());
        });

        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback -> {
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });
    }
}
