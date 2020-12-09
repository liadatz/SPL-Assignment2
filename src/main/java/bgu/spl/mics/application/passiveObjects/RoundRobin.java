package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.MicroService;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobin {
    private ArrayList<MicroService> list;
    private AtomicInteger index;

    public RoundRobin(){};
}
