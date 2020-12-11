package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobin {
//------------------------------------fields----------------------------------------------
    private ArrayList<MicroService> list;
    private AtomicInteger index;
//---------------------------------constructors-------------------------------------------
    public RoundRobin(){
        list = new ArrayList<>();
        index =  new AtomicInteger(0);
    };
//------------------------------------methods---------------------------------------------
    public void push(MicroService m){
        list.add(m);
    }
    public MicroService pop(){
        MicroService output = list.get(index.get());
        updateIndex();
        return output;
    }
    public void remove(MicroService m){
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
    public void updateIndex(){
        if (index.get() < list.size()-1)
            index.incrementAndGet();
        else
            index.set(0);
       }
       public boolean isEmpty(){
        return list.isEmpty();
       }
}
