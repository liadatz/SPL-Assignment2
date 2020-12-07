package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    //--------------------------fields--------------------------------------
    private AtomicInteger numOfAttackers;
    //--------------------------getters--------------------------------------
    public AtomicInteger getNumOfAttackers(){
        return numOfAttackers;
    }
    //--------------------------method--------------------------------------
    public void increaseNumOfAttackers(){
        numOfAttackers.incrementAndGet();
    }
}
