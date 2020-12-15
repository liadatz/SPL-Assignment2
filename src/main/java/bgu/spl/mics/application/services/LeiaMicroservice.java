package bgu.spl.mics.application.services;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 * this class extend the abstract class {@link MicroService}
 */
public class LeiaMicroservice extends MicroService {
//------------------------------------fields----------------------------------------------
    private Attack[] attacks;
	private Diary diary;
	private ConcurrentHashMap<Event, Future> futuresTable;
	private Future deactivationFuture;
//----------------------------------constructors------------------------------------------

    /**
     * constuct a new Leia Microservice
     * @param attacks is an array of all the attacks need to be created for the battle
     */
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		diary = Diary.getInstance();
		futuresTable = new ConcurrentHashMap<>();
		deactivationFuture = new Future();
    }
//------------------------------------methods---------------------------------------------
    /**
     * subscribes to all relevent events and broadcast, and define their callbacks aswell.
     * also creates all attacks and sends them to the Attackers Microservices
     */
    @Override
    protected void initialize() {
        System.out.println(this.getName() + " is initializing"); // (delete before submission)
        boolean isReady = false;
        // Wait until attackers are ready (registered and subscribed)
        while (!isReady) {
            try {
                Main.waitForAttackers.await();
                isReady = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Leia failed waiting for other attackers"); // (delete before submission)
            }
        }
        // Attack phase
        System.out.println("Leia start sending attacks"); // (delete before submission)
        for (Attack attack : attacks) {
            AttackEvent newAttack = new AttackEvent(attack.getSerials(), attack.getDuration());
            futuresTable.put(newAttack, sendEvent(newAttack)); //restore all futures
        }

        // Tell HanSolo and C3PO that there no more attacks
        FinishAttacksBroadcast finishAttacksBroadcast = new FinishAttacksBroadcast();
        sendBroadcast(finishAttacksBroadcast);

        //'Wait for attack to finish' phase
        System.out.println("Leia waits until all attacks are done"); // (delete before submission)
        for (Event key: futuresTable.keySet()) { //going through all Futures which where restores, and waiting for all to be resolved
                futuresTable.get(key).get(); //blocking if future is not resolve
        }
        System.out.println("Leia's attacks futures are all done"); // (delete before submission)

        //reach this point only after all Futures' 'get()' method succeed
        //sending Deactivation Event to R2D2
        DeactivationEvent deactivationEvent = new DeactivationEvent();
        deactivationFuture = sendEvent(deactivationEvent);
        System.out.println("Leia waits until deactivation done"); // (delete before submission)
        deactivationFuture.get(); //block and wait until deactivation Future is resolved
        System.out.println("Leia deactivation future is done"); // (delete before submission)

        //sending Bomb Destroy Event to Lando
        BombDestroyerEvent bombEvent = new BombDestroyerEvent();
        sendEvent(bombEvent); // notify Lando that shield deactivation is done

        // subscribe to TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback->{
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });
    }

}
