package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    public AtomicInteger getNumberOfAttacks() {
        return totalAttacks;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3P0Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public void resetNumberAttacks() {
        totalAttacks.set(0);
    }

    //--------------------------fields--------------------------------------
    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }
//    private AtomicInteger numOfAttackers;
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
    private Diary() {
//        numOfAttackers = new AtomicInteger(0);
        totalAttacks = new AtomicInteger(0);
    }
//--------------------------getters--------------------------------------
    public static Diary getInstance() {
        return SingletonHolder.instance;
    }
//    public AtomicInteger getNumOfAttackers() {
//        return numOfAttackers;
//    }
//--------------------------setter--------------------------------------
//    public void increaseNumOfAttackers() {
//        numOfAttackers.incrementAndGet();
//    }
    public void increaseTotalAttacks() {
        totalAttacks.incrementAndGet();
    }
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
    public void setFinishTime(MicroService m, long time) {
        String name = m.getName();
        switch (name) {
            case "Han":
                HanSoloFinish = time;
            case "C3PO":
                C3POFinish = time;
        }
    }
    public void setDeactivateTime(long time) {
        R2D2Deactivate = time;
    }
}
