package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DummyEvent;

public class DummyMicroService extends MicroService {

    public DummyMicroService(String string){
        super(string);
    }

    @Override
    protected void initialize() {
    }
}
