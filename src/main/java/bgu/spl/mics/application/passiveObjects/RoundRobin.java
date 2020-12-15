package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.MicroService;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class is an implementing of a queue of Microservices that works in according to the round-robin principle
 */
public class RoundRobin {
//------------------------------------fields----------------------------------------------
    private ArrayList<MicroService> list;
    private AtomicInteger index;
//---------------------------------constructors-------------------------------------------

    public RoundRobin(){
        list = new ArrayList<>();
        index =  new AtomicInteger(0);
    }
//------------------------------------methods---------------------------------------------

    /**
     * pushed new element to the queue
     * @param m is the microservice we want to push
     */
    public void push(MicroService m){
        list.add(m);
    }

    /**
     * pops one microservice out of the queue
     * @return the next microservice in line (according to the round- robin principle)
     */
    public MicroService pop(){
        MicroService output = list.get(index.get());
        updateIndex();
        return output;
    }

    /**
     * removes one microservice out of the queue (also from the middle or the end)
     * @param m is the microservice we want to remove
     */
    public void remove(MicroService m){ //removes an element from the queue and updating index
        int mLocation = list.indexOf(m);
        if (mLocation != -1) {
            if (mLocation < index.get()) {
                index.getAndDecrement();
            }
            else if (mLocation == index.get() & index.get() == list.size()){
                int expected = index.get();
                index.compareAndSet(expected, 0);
            }
            list.remove(m);
        }
    }

    /**
     * this method is used after poping a microservice. it updates the index of the next microservice which will be pulled
     * in a way the maintain the round- robin principle
     */
    public void updateIndex(){ //moves index forward unless it's on the last element. if so, index is reset to zero.
        if (index.get() < list.size()-1)
            index.getAndIncrement();
        else
            index.set(0);
       }

    /**
     *
     * @return true if the queue is empty, true otherwise
     */
    public boolean isEmpty(){
        return list.isEmpty();
       }
}
