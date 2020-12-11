package bgu.spl.mics.application.services;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
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
 */
public class LeiaMicroservice extends MicroService {
//------------------------------------fields----------------------------------------------
    private Attack[] attacks;
	private Diary diary;
	private ConcurrentHashMap<Event, Future> futuresTable;
	private Future deactivationFuture;
//----------------------------------constructors------------------------------------------
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		diary = Diary.getInstance();
		futuresTable = new ConcurrentHashMap<>();
		deactivationFuture = new Future();
    }
//------------------------------------methods---------------------------------------------
    @Override
    protected void initialize() {
        System.out.println(this.getName() + " is initializing"); // log
        // Wait until attackers are ready
        while (!diary.getNumOfAttackers().equals(new AtomicInteger(2))) {
            try {
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Leia failed waiting for other attackers"); // log
            }
        }
        // Attack phase
        System.out.println("Leia start sending attacks"); // log
        for (Attack attack : attacks) {
            AttackEvent newAttack = new AttackEvent(attack.getSerials(), attack.getDuration());
            futuresTable.put(newAttack, sendEvent(newAttack)); //keeps all futures. is using concurrentHashMap is enough? few threads have access to futures.
        }

        // Tell HanSolo and C3PO that there no more attacks
        FinishAttacksBroadcast finishAttacksBroadcast = new FinishAttacksBroadcast();
        sendBroadcast(finishAttacksBroadcast);

        //'Wait for attack to finish' phase
        System.out.println("Leia waits until all attacks are done"); // log
        for (Event key: futuresTable.keySet()) {
                futuresTable.get(key).get(); //blocking if future is not resolved. is it enough? do we need to check if return value is indeed true? because in our program it will never be false
        }
        System.out.println("Leia's attacks futures are all done"); // log

        //reach this point only after all futrues 'get()' method succeed
        DeactivationEvent deactivationEvent = new DeactivationEvent();
        deactivationFuture = sendEvent(deactivationEvent);
        System.out.println("Leia waits until deactivation done"); // log
        deactivationFuture.get(); //block and wait until deactivation future is resolved
        System.out.println("Leia deactivation future is done"); // log

        BombDestroyerEvent bombEvent = new BombDestroyerEvent();
        sendEvent(bombEvent); // notify Lando that shield deactivation is done
        //is needed to get answer from lando?
        // TerminateBroadcast
        subscribeBroadcast(TerminateBroadcast.class, callback->{
            terminate();
            diary.setTerminateTime(this, System.currentTimeMillis());
        });
    }

}
