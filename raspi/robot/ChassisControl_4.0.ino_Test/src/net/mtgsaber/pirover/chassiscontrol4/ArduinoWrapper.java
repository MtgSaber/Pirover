package net.mtgsaber.pirover.chassiscontrol4;

import arduino.Arduino;

/**
 * Author: Andrew Arnold (7/16/2017)
 */
class ArduinoWrapper {
    private static boolean inUse;
    private static boolean connected;
    private static Arduino arduino;
    private static Object key;

    public static synchronized Object activate(String portDescription, int bitrate)
        throws AlreadyInUseException, ConnectionFailedException
    {
        if (inUse) throw new AlreadyInUseException();
        inUse = true;
        arduino = new Arduino(portDescription, bitrate);
        connected = arduino.openConnection();
        if (!connected) throw new ConnectionFailedException();
        arduino.serialWrite('~');
        key = new Object();
        return key;
    }
    
    public static synchronized void deactivate(Object keyOffer) 
        throws AlreadyInUseException {
        if (key != keyOffer) throw new AlreadyInUseException();
        connected = false;
        arduino.closeConnection();
        key = null;
        inUse = false;
    }

    public static synchronized Arduino getArduino(Object keyOffer)
            throws AlreadyInUseException {
        if (key != keyOffer) throw new AlreadyInUseException();
        else return arduino;
    }
}
