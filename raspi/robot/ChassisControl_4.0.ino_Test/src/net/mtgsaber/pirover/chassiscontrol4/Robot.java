package net.mtgsaber.pirover.chassiscontrol4;

import arduino.Arduino;
import com.fazecast.jSerialComm.SerialPort;

/**
 * Author: Andrew Arnold (7/16/2017)
 *
 * Driver for Arduino Uno. Can be driven by another class by using setState.
 */
class Robot {
    private StringBuffer status;
    private MotorState curMotorState;
    private Arduino arduino;
    private Object arduinoKey;

    public static final char[] HEX_SYMBOLS = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    Robot()
            throws AlreadyInUseException,
                   ConnectionFailedException {
        this.curMotorState = MotorState.COAST;
        this.status = new StringBuffer(curMotorState.getName());
        this.arduinoKey = ArduinoWrapper.activate(SerialPort.getCommPorts()[0].getSystemPortName(), 9600);
        this.arduino = ArduinoWrapper.getArduino(arduinoKey);
    }

    void setStates(MotorState leftMotorState, MotorState rightMotorState, int leftSpeed, int rightSpeed) {
        final char[] message = new char[] {
                '~','A','0','0','A','0','0',
                '0','0','0','0','0','0','0','0','^'
        };

        message[1] = leftMotorState.getKey();
        message[4] = rightMotorState.getKey();

        if (leftSpeed < 0) leftSpeed = leftSpeed * -1;
        if (leftSpeed > 255) leftSpeed = leftSpeed % 255;
        if (rightSpeed < 0) leftSpeed = leftSpeed * -1;
        if (rightSpeed > 255) leftSpeed = leftSpeed % 255;

        message[2] = HEX_SYMBOLS[leftSpeed/16];
        message[3] = HEX_SYMBOLS[leftSpeed%16];
        message[5] = HEX_SYMBOLS[rightSpeed/16];
        message[6] = HEX_SYMBOLS[rightSpeed%16];

        arduino.serialWrite(new String(message), message.length, 1);
    }

    String getStatus() {
        return status.toString();
    }

    void start() {
        this.arduino.serialWrite('~');
    }

    void end()
            throws AlreadyInUseException {
        this.arduino = null;
        ArduinoWrapper.deactivate(arduinoKey);
        arduinoKey = null;
        this.curMotorState = MotorState.COAST;
    }
}
