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
        if (list.indexOf(m) < index.get()) {
            index.getAndDecrement();
        }
        else if (list.indexOf(m) == index.get() & index.get() == list.size()){
            index.set(0);
        }
        list.remove(m);
    }
    public void updateIndex(){
        if (index.get() < list.size()-1)
            index.incrementAndGet();
        else
            index.set(0);
       }
}
