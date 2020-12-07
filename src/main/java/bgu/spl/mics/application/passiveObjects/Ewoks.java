package bgu.spl.mics.application.passiveObjects;


import java.util.Iterator;
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
    private Ewok[] ewoks;
    private static class SingletonHolder {
        private static Ewoks instance = null;
        private SingletonHolder(int numOfEwoks){
            instance = new Ewoks(numOfEwoks);
        }
    }

    private Ewoks(int numOfEwoks) {
        ewoks = new Ewok[numOfEwoks];
        for (int i=0; i<numOfEwoks; i++){
            ewoks[i] = new Ewok(i+1);
        }
    }


    public static Ewoks getInstance(int numOfEwoks) {
        SingletonHolder sh = new SingletonHolder(numOfEwoks);
        return SingletonHolder.instance;
    }

    public static Ewoks getInstance() {
        return SingletonHolder.instance;
    }

    public void acquireEwoks(List<Integer> serials){

    }

    public void releaseEwoks(List<Integer> serials){
        Iterator<Integer> iter = serials.iterator();
        while(iter.hasNext()){
            ewoks[iter.next()-1].release();
        }
    }
}
