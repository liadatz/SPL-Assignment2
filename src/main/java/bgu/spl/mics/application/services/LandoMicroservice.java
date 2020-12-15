package bgu.spl.mics.application.services;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 * his class extends the abstract class {@link MicroService}
 */
public class LandoMicroservice  extends MicroService {
//------------------------------------fields----------------------------------------------
    private long duration;
    Diary diary;
//----------------------------------constructors------------------------------------------

    /**
     * Construct a new lando microservice
     * @param duration is the time required to this lando microservice to activate the bomb
     */
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        diary = Diary.getInstance();
    }
//------------------------------------methods---------------------------------------------

    /**
     * subscribes to all relevent events and broadcast, and define their callbacks aswell.
     */
    @Override
    protected void initialize() {
        System.out.println(this.getName() + " is initializing"); // (delete before submission)
        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback->{
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });

        // BombDestroyerEvent
        Callback<BombDestroyerEvent> DeCallback = (BombDestroyerEvent e)->{
            System.out.println(this.getName() + " is handling BombDestroyerEvent"); // (delete before submission)
            try {
                MILLISECONDS.sleep(duration);
            }
            catch (InterruptedException eX){
                eX.printStackTrace();
            }
            TerminateBroadcast terminate = new TerminateBroadcast();
            sendBroadcast(terminate); //notify all microservices that the attack was done
            System.out.println(this.getName() + " is finish handling BombDestroyerEvent and sent TerminateBroadcast"); // (delete before submission)
        };
        subscribeEvent(BombDestroyerEvent.class, DeCallback);

    }
}
