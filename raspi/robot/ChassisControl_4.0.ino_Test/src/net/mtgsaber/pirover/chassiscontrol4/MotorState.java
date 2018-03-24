package net.mtgsaber.pirover.chassiscontrol4;

/**
 * Author: Andrew Arnold (7/10/2017)
 *
 * Enumerates the various states in which the Pirover motors operate.
 */
enum MotorState {
    CLOCKWISE (((char) 0x01), "Left"),
    CCLOCKWISE (((char) 0x02), "Right"),
    COAST (((char) 0x00), "Coast"),
    BRAKE (((char) 0x03), "Brake");

    public final char byt;
    public final String name;

    MotorState(char byt, String name) {
        this.byt = byt;
        this.name = name;
    }

    public static char[] getKeysUsed() {
        char[] keys = new char[MotorState.values().length];
        for (int i=0; i < keys.length; i++)
            keys[i] = MotorState.values()[i].byt;
        return keys;
    }

    public static MotorState getByByte(char byt) {
        for (MotorState motorState : values())
            if (motorState.byt == byt)
                return motorState;
        return null;
    }

    public static MotorState getByName(String name) {
        for (MotorState motorState : values())
            if (motorState.name.equals(name))
                return motorState;
        return null;
    }
}