package net.mtgsaber.pirover.chassiscontrol4;

import arduino.Arduino;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Author: Andrew Arnold (7/16/2017)
 *
 * Driver for Arduino Uno. Can be driven by another class by using setState.
 */
public class Robot {
    private StringBuilder statesAsString;
    private MotorState leftMotor, rightMotor;
    private int leftSpeed, rightSpeed, servo1, servo2, servo3, servo4;
    private Arduino arduino;
    private Object arduinoKey;
    private boolean available, on;

    public Robot()
            throws AlreadyInUseException,
                   ConnectionFailedException {
        this.arduinoKey = ArduinoWrapper.activate(SerialPort.getCommPorts()[0].getSystemPortName(), 9600);
        this.arduino = ArduinoWrapper.getArduino(arduinoKey);

        leftMotor = MotorState.BRAKE;
        rightMotor = MotorState.BRAKE;

        leftSpeed = 0; rightSpeed = 0;
        servo1 = 0; servo2 = 0;
        servo3 = 0; servo4 = 0;

        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);

        on = false;
        available = true;
    }

    public void setStates(
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

        byte msbs = 0x00;

        if (servo4 == 0x7F) servo4--;
        else if (servo4 > 0x7F) {
            msbs += 1;
            servo4 -= 0x80;
        }
        if (servo3 == 0x7F) servo3--;
        else if (servo3 > 0x7F) {
            msbs += 2;
            servo3 -= 0x80;
        }
        if (servo2 == 0x7F) servo2--;
        else if (servo2 > 0x7F) {
            msbs += 4;
            servo2 -= 0x80;
        }
        if (servo1 == 0x7F) servo1--;
        else if (servo1 > 0x7F) {
            msbs += 8;
            servo1 -= 0x80;
        }
        if (leftSpeed == 0x7F) leftSpeed--;
        else if (leftSpeed > 0x7F) {
            msbs += 16;
            leftSpeed -= 0x80;
        }
        if (rightSpeed == 0x7F) rightSpeed--;
        else if (rightSpeed > 0x7F) {
            msbs += 32;
            rightSpeed -= 0x80;
        }

        synchronized (this) {
            leftMotor = leftMotorState;
            rightMotor = rightMotorState;
            this.leftSpeed = leftSpeed;
            this.rightSpeed = rightSpeed;
            this.servo1 = servo1;
            this.servo2 = servo2;
            this.servo3 = servo3;
            this.servo4 = servo4;

            statesAsString = new StringBuilder(new String(new char[]{
                    (char) (leftMotor.byt * 4 + rightMotor.byt),
                    (char) (msbs),
                    (char) (leftSpeed),
                    (char) (rightSpeed),
                    (char) (servo1),
                    (char) (servo2),
                    (char) (servo3),
                    (char) (servo4),
            }));
        }
    }

    public synchronized void setLeftMotor(MotorState leftMotor) {
        setStates(leftMotor, rightMotor,
                (leftMotor == MotorState.COAST || leftMotor == MotorState.BRAKE ? 0 : leftSpeed),
                rightSpeed, servo1, servo2, servo3, servo4);
    }
    public synchronized void setRightMotor(MotorState rightMotor) {
        setStates(leftMotor, rightMotor, leftSpeed,
                (rightMotor == MotorState.COAST || rightMotor == MotorState.BRAKE ? 0 : rightSpeed),
                servo1, servo2, servo3, servo4);
    }
    public synchronized void setLeftSpeed(int leftSpeed) {
        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);
    }
    public synchronized void setRightSpeed(int rightSpeed) {
        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);
    }
    public synchronized void setServo1(int servo1) {
        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);
    }
    public synchronized void setServo2(int servo2) {
        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);
    }
    public synchronized void setServo3(int servo3) {
        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);
    }
    public synchronized void setServo4(int servo4) {
        setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, servo1, servo2, servo3, servo4);
    }

    public MotorState getLeftMotor() {
        return leftMotor;
    }
    public MotorState getRightMotor() {
        return rightMotor;
    }
    public int getLeftSpeed() {
        return leftSpeed;
    }
    public int getRightSpeed() {
        return rightSpeed;
    }
    public int getServo1() {
        return servo1;
    }
    public int getServo2() {
        return servo2;
    }
    public int getServo3() {
        return servo3;
    }
    public int getServo4() {
        return servo4;
    }

    public synchronized void writeStates() {
        arduino.serialWrite("" + ((char) 0x7F) + getStatus() + ((char) 0x7F), getStatus().length()+2, 1);
    }

    public String getStatus() {
        return statesAsString.toString();
    }

    public synchronized void start() {
        available = true;
        on = true;
    }

    public synchronized void end()
            throws AlreadyInUseException {
        this.arduino = null;
        ArduinoWrapper.deactivate(arduinoKey);
        arduinoKey = null;
        available = false;
        on = false;
    }

    public boolean isAvailable() { return available; }
    public boolean isOn() { return on; }
}
