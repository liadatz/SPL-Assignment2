package bgu.spl.mics.application.services;
import bgu.spl.mics.application.messages.AttackEvent;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 * this class extends the abstract class {@link AttackersMicroservice}
 */
public class C3POMicroservice extends AttackersMicroservice {
    //----------------------------------constructors------------------------------------------
    public C3POMicroservice() {
        super("C3PO");
    }
}
