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
     *   @param serialNumbers is a list of ID's of the ewoks required for this attack - initialized to field serials
     *   @param duration is the time takes to complete this attack - initialized to field duration
     *
     */
    public Attack(List<Integer> serialNumbers, int duration) {
        Collections.sort(serialNumbers); //sorting Ewoks serial number in order to prevent possible dead- blocks
        this.serials = serialNumbers;
        this.duration = duration;
    }
//------------------------------------getters---------------------------------------------
    /**
     * @return list of ID's of the ewoks required for the attack
     */
    public List<Integer> getSerials() {
        return serials;
    }
    /**
     * @return time that required for the attack
     */
    public int getDuration() {
        return duration;
    }
}
