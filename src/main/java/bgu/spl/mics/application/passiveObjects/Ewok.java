package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
//------------------------------------fields----------------------------------------------
    int serialNumber;
    boolean available;
//----------------------------------constructors------------------------------------------
    public Ewok(int serialNumber) {
        this.serialNumber = serialNumber;
        available = true;
    }
//------------------------------------getters---------------------------------------------
    public boolean getAvailable() {
        return available;
    }
//------------------------------------methods---------------------------------------------
    /**
     * Acquires an Ewok
     */
    public synchronized void acquire() {
        while (!available)
            try{wait();}
        catch(InterruptedException e){
                System.out.println("Failed acquiring Ewoks"); // log
                e.printStackTrace();
            }
        available = false;
    }
    /**
     * release an Ewok
     */
    public synchronized void release() {
        available = true;
        notifyAll();
    }
}
