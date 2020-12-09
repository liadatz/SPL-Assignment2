package bgu.spl.mics.application.passiveObjects;
import java.util.Collections;
import java.util.List;


/**
 * Passive data-object representing an attack object.
 * You must not alter any of the given public methods of this class.
 * <p>
 * YDo not add any additional members/method to this class (except for getters).
 */
public class Attack {
//------------------------------------fields----------------------------------------------
    final List<Integer> serials;
    final int duration;
//----------------------------------constructors------------------------------------------
    /**
     * Constructor.
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        Collections.sort(serialNumbers);
        this.serials = serialNumbers;
        this.duration = duration;
    }
//------------------------------------getters---------------------------------------------
    public List<Integer> getSerials() {
        return serials;
    }
    public int getDuration() {
        return duration;
    }





}
