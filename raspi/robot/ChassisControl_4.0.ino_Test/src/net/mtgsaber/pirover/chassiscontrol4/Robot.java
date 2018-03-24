package net.mtgsaber.pirover.chassiscontrol4;

import arduino.Arduino;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Author: Andrew Arnold (7/16/2017)
 *
 * Driver for Arduino Uno. Can be driven by another class by using setState.
 */
class Robot {
    private StringBuilder statesAsString;
    private MotorState leftMotor, rightMotor;
    private int leftSpeed, rightSpeed, servo1, servo2, servo3, servo4;
    private Arduino arduino;
    private Object arduinoKey;
    private boolean available, on;

    Robot()
            throws AlreadyInUseException,
                   ConnectionFailedException {
        this.arduinoKey = ArduinoWrapper.activate(SerialPort.getCommPorts()[0].getSystemPortName(), 9600);
        this.arduino = ArduinoWrapper.getArduino(arduinoKey);

        leftMotor = MotorState.BRAKE;
        rightMotor = MotorState.BRAKE;

        leftSpeed = 0; rightSpeed = 0;
        servo1 = 0; servo2 = 0;
        servo3 = 0; servo4 = 0;

        available = true;
    }

    void setStates(
            MotorState leftMotorState, MotorState rightMotorState,
            int leftSpeed, int rightSpeed,
            int servo1, int servo2, int servo3, int servo4
    ) {
        if (0 > leftSpeed || leftSpeed > 0xFE) return;
        if (0 > rightSpeed || rightSpeed > 0xFE) return;
        if (0 > servo1 || servo1 > 0xB4) return;
        if (0 > servo2 || servo2 > 0xB4) return;
        if (0 > servo3 || servo3 > 0xB4) return;
        if (0 > servo4 || servo4 > 0xB4) return;

        leftMotor = leftMotorState;
        rightMotor = rightMotorState;
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
        this.servo1 = servo1;
        this.servo2 = servo2;
        this.servo3 = servo3;
        this.servo4 = servo4;
    }

    void writeStates() {
        statesAsString = new StringBuilder(new String(new char[] {
                (char)(leftMotor.byt * 16 + rightMotor.byt),
                (char)(leftSpeed),
                (char)(rightSpeed),
                (char)(servo1),
                (char)(servo2),
                (char)(servo3),
                (char)(servo4),
        }));

        arduino.serialWrite("" + ((char) 0xFF) + getStatus() + ((char) 0xFF), getStatus().length(), 1);
        System.out.println(arduino.serialRead());
    }

    String getStatus() {
        return statesAsString.toString();
    }

    void start() {
        this.arduino.serialWrite('~');
        available = true;
        on = true;
    }

    void end()
            throws AlreadyInUseException {
        this.arduino = null;
        ArduinoWrapper.deactivate(arduinoKey);
        arduinoKey = null;
        available = false;
        on = false;
    }
}
