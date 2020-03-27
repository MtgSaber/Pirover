package net.mtgsaber.pirover.chassiscontrol4;

/**
 * Author: Andrew Arnold (7/30/2017)
 */
public class ConnectionFailedException extends Exception {
    @Override
    public String getMessage() {
        return "Arduino Serial Connection failed!";
    }
}
