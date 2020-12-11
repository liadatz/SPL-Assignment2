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
 */
public class LandoMicroservice  extends MicroService {
//------------------------------------fields----------------------------------------------
    private long duration;
    Diary diary;
//----------------------------------constructors------------------------------------------
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        diary = Diary.getInstance();
    }
//------------------------------------methods---------------------------------------------
    @Override
    protected void initialize() {
        System.out.println(this.getName() + " is initializing"); // log
        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback->{
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });

        // BombDestroyerEvent
        Callback<BombDestroyerEvent> DeCallback = (BombDestroyerEvent e)->{
            System.out.println(this.getName() + " is handling BombDestroyerEvent"); // log
            try {
                MILLISECONDS.sleep(duration);
            }
            catch (InterruptedException eX){
                eX.printStackTrace();
            }
            TerminateBroadcast terminate = new TerminateBroadcast();
            System.out.println("enter");
            sendBroadcast(terminate); //notify all microservices that the attack was done
            System.out.println("finish");
            System.out.println(this.getName() + " is finish handling BombDestroyerEvent and sent TerminateBroadcast"); // log
        };
        subscribeEvent(BombDestroyerEvent.class, DeCallback);
    }
}
