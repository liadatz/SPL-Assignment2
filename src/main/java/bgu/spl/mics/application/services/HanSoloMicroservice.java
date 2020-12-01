package bgu.spl.mics.application.services;


import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private Integer numOfSubscribers;

    public HanSoloMicroservice() {
        super("Han");
    }

    public HanSoloMicroservice(Integer numOfSubscribers){
        super("Han");
        this.numOfSubscribers = numOfSubscribers;
    }


    @Override
    protected void initialize() {

    }
}
