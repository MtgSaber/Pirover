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

        byte msbs = 0x00;

        if (servo4 == 0x7F) servo4--;
        else if (servo4 > 0x80) {
            msbs += 1;
            servo4 -= 0x80;
        }
        if (servo3 == 0x7F) servo3--;
        else if (servo3 > 0x80) {
            msbs += 2;
            servo3 -= 0x80;
        }
        if (servo2 == 0x7F) servo2--;
        else if (servo2 > 0x80) {
            msbs += 4;
            servo2 -= 0x80;
        }
        if (servo1 == 0x7F) servo1--;
        else if (servo1 > 0x80) {
            msbs += 8;
            servo1 -= 0x80;
        }
        if (leftSpeed == 0x7F) leftSpeed--;
        else if (leftSpeed > 0x80) {
            msbs += 16;
            leftSpeed -= 0x80;
        }
        if (rightSpeed == 0x7F) rightSpeed--;
        else if (rightSpeed > 0x80) {
            msbs += 32;
            rightSpeed -= 0x80;
        }

        statesAsString = new StringBuilder(new String(new char[] {
                (char)(leftMotor.byt * 16 + rightMotor.byt),
                (char)(msbs),
                (char)(leftSpeed),
                (char)(rightSpeed),
                (char)(servo1),
                (char)(servo2),
                (char)(servo3),
                (char)(servo4),
        }));
    }

    void writeStates() {
        arduino.serialWrite("" + ((char) 0x7F) + getStatus() + ((char) 0x7F));
        String feedback = arduino.serialRead();
        System.out.println("Arduino Interpreted String:\n\t\"" + feedback + "\"\n\tLength: " + feedback.length());
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
