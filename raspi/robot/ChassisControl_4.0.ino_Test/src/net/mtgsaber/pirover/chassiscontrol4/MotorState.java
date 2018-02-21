package net.mtgsaber.pirover.chassiscontrol4;

/**
 * Author: Andrew Arnold (7/10/2017)
 *
 * Enumerates the various states in which the Pirover motors operate.
 */
enum MotorState {
    CLOCKWISE ('C', "Left"),
    CCLOCKWISE ('D', "Right"),
    COAST ('A', "Coast"),
    BRAKE ('B', "Brake");

    private char key;
    private String name;

    MotorState(char key, String name) {
        this.key = key;
        this.name = name;
    }

    public char getKey() { return this.key; }
    public String getName() { return this.name; }

    public static char[] getKeysUsed() {
        char[] keys = new char[MotorState.values().length];
        for (int i=0; i < keys.length; i++)
            keys[i] = MotorState.values()[i].getKey();
        return keys;
    }

    public static MotorState getByKey(char key) {
        for (MotorState motorState : values())
            if (motorState.getKey() == key)
                return motorState;
        return null;
    }

    public static MotorState getByName(String name) {
        for (MotorState motorState : values())
            if (motorState.getName().equals(name))
                return motorState;
        return null;
    }
}