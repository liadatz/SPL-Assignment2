package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;


/**
 * this class extends the abstract class {@link MicroService}
 * used for testing
 */
public class DummyMicroService extends MicroService {
    public DummyMicroService(String string){
        super(string);
    }
    @Override
    protected void initialize() {
    }
}
