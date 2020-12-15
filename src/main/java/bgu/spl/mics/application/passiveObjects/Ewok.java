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
    /**
     *  @param serialNumber is the ID of the ewok - initialized to field serials
     * available is intialized to be true at first
     */
    public Ewok(int serialNumber) {
        this.serialNumber = serialNumber;
        available = true;
    }
//------------------------------------getters---------------------------------------------

    /**
     *
     * @return true if available and false otherwise
     */
    public boolean getAvailable() {
        return available;
    }
//------------------------------------methods---------------------------------------------
    /**
     * Acquires an Ewok
     * locks every single Ewok when trying to acquire
     */
    public synchronized void acquire() {
        while (!available)
            try{wait();}
        catch(InterruptedException e){
                System.out.println("Failed acquiring Ewoks"); // log (delete before submission)
                e.printStackTrace();
            }
        available = false;
    }
    /**
     * release an Ewok
     * locks every single Ewok when trying to release, if released notify all and allows other to try and acquire
     */
    public synchronized void release() {
        available = true;
        notifyAll();
    }
}
