package bgu.spl.mics.application.passiveObjects;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
//------------------------------------fields----------------------------------------------
    private Ewok[] ewoks;
    private static class SingletonHolder {
        private static Ewoks instance = null;
        private SingletonHolder(int numOfEwoks) { //single instance makes Ewoks class a singleton
            instance = new Ewoks(numOfEwoks);
        }
    }
//---------------------------------constructors-------------------------------------------
    /**
     *  @param numOfEwoks the number of ewoks we need to create - initialized as an array of integers to field ewoks
     */
    private Ewoks(int numOfEwoks) { //no public access, will be construct only once
        ewoks = new Ewok[numOfEwoks];
        for (int i = 0; i < numOfEwoks; i++) { //initialize list of ewoks with the size of 'numOfEwoks'
            ewoks[i] = new Ewok(i + 1);
        }
    }
//------------------------------------getters---------------------------------------------

    /**
     * @param numOfEwoks is the number of ewoks we need to create
     * @return single instance of Ewoks- this is a safe-thread singleton
     */
    public static Ewoks getInstance(int numOfEwoks) { //public access, used as constructor and after construct will return instance
        SingletonHolder sh = new SingletonHolder(numOfEwoks);
        return SingletonHolder.instance;
    }

    /**
     *
     * @return single instance of Ewoks- this is a safe-thread singleton
     */
    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }
//-----------------------------------methods---------------------------------------------

    /**
     * acquires a list of ewoks one by one
     * @param serials is a list of the ewoks we want to acquire
     */
    public void acquireEwoks(List<Integer> serials) {
        for (Integer serial : serials) {
                ewoks[serial - 1].acquire();
            }
    }
    /**
     * releases a list of ewoks one by one
     * @param serials is a list of the ewoks we want to release
     */
    public void releaseEwoks(List<Integer> serials) {
        for (Integer serial : serials) {
                ewoks[serial - 1].release();
            }
        }
}
