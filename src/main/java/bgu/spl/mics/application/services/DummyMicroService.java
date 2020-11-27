package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;

public class DummyMicroService extends MicroService {

    public DummyMicroService(String string){
        super(string);
    }


    @Override
    protected void initialize() {
    }
}
