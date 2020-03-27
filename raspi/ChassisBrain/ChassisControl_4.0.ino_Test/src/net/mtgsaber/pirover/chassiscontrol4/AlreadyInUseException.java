package net.mtgsaber.pirover.chassiscontrol4;

/**
 * Author: Andrew Arnold (7/30/2017)
 */
public class AlreadyInUseException extends Exception {
    @Override
    public String getMessage() {
        return "This class is already in use!";
    }
}
