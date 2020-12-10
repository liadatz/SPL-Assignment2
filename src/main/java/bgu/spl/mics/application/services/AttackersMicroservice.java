package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishAttacksBroadcast;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public abstract class AttackersMicroservice extends MicroService {
    //------------------------------------fields----------------------------------------------
    private Diary diary;
    private Ewoks ewoks;
    //----------------------------------constructors------------------------------------------
    public AttackersMicroservice(String name) {
        super(name);
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
        subscribeBroadcast(TerminateBroadcast.class, callback -> {
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });

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