package net.mtgsaber.pirover.prototype.a;

import net.mtgsaber.pirover.chassiscontrol4.AlreadyInUseException;
import net.mtgsaber.pirover.chassiscontrol4.MotorState;
import net.mtgsaber.pirover.chassiscontrol4.Robot;

/**
 * Author: Andrew Arnold (4/25/2018)
 */
public class RobotTicker implements Clock.Tickable {
    public final Robot robot;
    private MotorState leftMotor, rightMotor;
    private int leftSpeed, rightSpeed;

    public RobotTicker(Robot robot) {
        this.robot = robot;
        leftMotor = MotorState.BRAKE;
        rightMotor = MotorState.BRAKE;
        leftSpeed = 0;
        rightSpeed = 0;
    }

    public synchronized void tick() {
        if (robot.isOn()) {
            robot.setStates(leftMotor, rightMotor, leftSpeed, rightSpeed, 0, 0, 0, 0);
            robot.writeStates();
        }
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
    public synchronized void setLeftMotor(MotorState leftMotor) {
        this.leftMotor = leftMotor;
    }
    public synchronized void setRightMotor(MotorState rightMotor) {
        this.rightMotor = rightMotor;
    }
    public synchronized void setLeftSpeed(int leftSpeed) {
        this.leftSpeed = leftSpeed;
    }
    public synchronized void setRightSpeed(int rightSpeed) {
        this.rightSpeed = rightSpeed;
    }

    public synchronized void off() {
        if (robot.isOn())
            try {
                robot.end();
            } catch (AlreadyInUseException aiuex) {
                aiuex.printStackTrace();
            }
    }

    public synchronized void on() {
        if (!robot.isOn())
            robot.start();
    }
}
