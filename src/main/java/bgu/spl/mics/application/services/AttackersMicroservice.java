package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishAttacksBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * abstract class for Microservices that function as attackers. in out implementation: HanSolo, C3PO
 */

public abstract class AttackersMicroservice extends MicroService {
    //------------------------------------fields----------------------------------------------

    private Diary diary;
    private Ewoks ewoks;
    //----------------------------------constructors------------------------------------------

    /**
     * construct new AttackerMicroservice
     * @param name is the name of the microservice
     */
    public AttackersMicroservice(String name) {
        super(name);
        diary = Diary.getInstance();
        ewoks = Ewoks.getInstance();
    }
    //------------------------------------methods---------------------------------------------

    /**
     * subscribes to all relevent events and broadcast, and define their callbacks aswell.
     */
    @Override
    protected void initialize() {
        System.out.println(this.getName() + " is initializing"); // (delete before submission)
        // Attacks
        Callback<AttackEvent> attackCallback = (AttackEvent e) -> {
            System.out.println(this.getName() + " is handling attack"); // log
            ewoks.acquireEwoks(e.getEwoksSerials()); //blocking method
            try {
                MILLISECONDS.sleep(e.getDuration());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            ewoks.releaseEwoks(e.getEwoksSerials());
            complete(e, true);
            diary.increaseTotalAttacks();
            System.out.println(this.getName() + " is finish handling attack"); // (delete before submission)
        };
        subscribeEvent(AttackEvent.class, attackCallback);
        Main.waitForAttackers.countDown();

        // FinishAttacks
        //FinishAttacksBroadcast will be the first message in the queue when all attacks are finished
        subscribeBroadcast(FinishAttacksBroadcast.class, callback -> diary.setFinishTime(this, System.currentTimeMillis()));
        Main.waitForAttackers.countDown();

        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback -> {
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });
    }
}
