package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MicroService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    //--------------------------fields--------------------------------------
    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }
    /**
     * @param totalAttacks is the number of attacks completed so far
     * @param HanSoloFinish is Microservise Han Solo's Finish attacks time
     * @param C3POFinish is Microservise C3PO's Finish attacks time
     * @param R2D2Deactivate is the Microservise R2D2 time of deactivating the shield
     * all terminate fields stand for one of the active Microservices termination time
     */
    private AtomicInteger totalAttacks;
    private long HanSoloFinish = 0;
    private long C3POFinish = 0;
    private long R2D2Deactivate = 0;
    private long HanSoloTerminate = 0;
    private long C3P0Terminate = 0;
    private long LeiaTerminate = 0;
    private long R2D2Terminate = 0;
    private long LandoTerminate = 0;

    //------------------------constructor------------------------------------
    /**
     * being called only once. makes Diary class a thread-safe singleton
     */
    private Diary() {
        totalAttacks = new AtomicInteger(0);
    }
//--------------------------getters--------------------------------------

    /**
     * @return SingletonHolder.instance- Diary is a singleton
     */
    public static Diary getInstance() {
        return SingletonHolder.instance;
    }
//--------------------------setters--------------------------------------

    /**
     * increases number of total attacks by one
     * uses thread- safe atomic function incrementAndGet
     */
    public void increaseTotalAttacks() {
        totalAttacks.incrementAndGet();
    }
    /**
     * sets the terminate time of specific Microservice
     * @param m is the specific microservice
     * @param time is the termination time of this microservice
     */
    public void setTerminateTime(MicroService m, long time) {
        String name = m.getName();
        switch (name) {
            case "Han":
                HanSoloTerminate = time;
            case "C3PO":
                C3P0Terminate = time;
            case "Leia":
                LeiaTerminate = time;
            case "R2D2":
                R2D2Terminate = time;
            case "Lando":
                LandoTerminate = time;
        }
    }
    /**
     * sets the finish time of specific Microservice attacker
     * @param m is the specific microservice attacker
     * @param time is the time this microservice finished all his attacks
     */
    public void setFinishTime(MicroService m, long time) {
        String name = m.getName();
        switch (name) {
            case "Han":
                HanSoloFinish = time;
            case "C3PO":
                C3POFinish = time;
        }
    }
    /**
     * sets the deactivation time
     * @param time is the deactivation time
     */
    public void setDeactivateTime(long time) {
        R2D2Deactivate = time;
    }
}
