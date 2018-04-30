package net.mtgsaber.pirover.prototype.a;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import net.mtgsaber.pirover.chassiscontrol4.MotorState;
import net.mtgsaber.pirover.chassiscontrol4.Robot;

import java.util.ArrayList;

/**
 * Author: Andrew Arnold (4/19/2018)
 */
public class ControlPaneEvents {
    private final ControlPane controlPane;
    private final ArrayList<KeyCode> keysPressed;
    private final ArrayList<MouseButton> mouseButtonsPressed;

    //public final Clock robotClock = new Clock(200);
    public final RobotTicker robot;
    public MouseCombo curMouseCombo;

    public final KeyCode[]
            LEFT_STATE_KEYS = new KeyCode[] {KeyCode.Q, KeyCode.A, KeyCode.Z},
            RIGHT_STATE_KEYS = new KeyCode[] {KeyCode.E, KeyCode.D, KeyCode.C};

    public enum MouseCombo {
        EQUALIZE_SPDS (new MouseButton[] {MouseButton.MIDDLE}),
        SYNC_SPDS_TO_LEFT (new MouseButton[] {MouseButton.PRIMARY, MouseButton.MIDDLE}),
        SYNC_SPDS_TO_RIGHT (new MouseButton[] {MouseButton.SECONDARY, MouseButton.MIDDLE}),
        LEFT_SPD_SET (new MouseButton[] {MouseButton.PRIMARY}),
        RIGHT_SPD_SET (new MouseButton[] {MouseButton.SECONDARY});

        public MouseButton[] combo;

        MouseCombo(MouseButton[] combo) {
            this.combo = combo;
        }

        public static MouseCombo getByCombo(MouseButton[] combo) {
            boolean succ;
            final boolean[][] usedFlags = new boolean[2][];
            int length;

            for (MouseCombo mouseCombo : values()) {
                succ = true;
                if (mouseCombo.combo.length == combo.length) {
                    length = combo.length;
                    usedFlags[0] = new boolean[length];
                    usedFlags[1] = new boolean[length];

                    for (int i=0; i < length; i++) {
                        for (int j=0; j < length && !usedFlags[0][i]; j++) {
                            if (!usedFlags[1][j])
                                if (mouseCombo.combo[i] == combo[j]) {
                                    usedFlags[0][i] = true;
                                    usedFlags[1][j] = true;
                                }
                        }
                    }
                    for (boolean bool : usedFlags[0])
                        if (!bool)
                            succ = false;
                    for (boolean bool : usedFlags[1])
                        if (!bool)
                            succ = false;
                } else succ = false;
                if (succ)
                    return mouseCombo;
            }
            return null;
        }
    }

    public ControlPaneEvents(ControlPane controlPane, Robot robot) {
        this.controlPane = controlPane;
        this.robot = new RobotTicker(robot);

        keysPressed = new ArrayList<>();
        mouseButtonsPressed = new ArrayList<>();
    }

    public void hookEvents() {
        controlPane.btStart.setOnAction(event -> actionBTStart());
        controlPane.btStop.setOnAction(event -> actionBTStop());
        controlPane.tfControlField.setOnKeyPressed(event -> {
            if (!keysPressed.contains(event.getCode())) {
                keysPressed.add(event.getCode());
                interpretKeysPressed();
            }
        });
        controlPane.tfControlField.setOnKeyReleased(event -> {
            if (keysPressed.contains(event.getCode())) {
                keysPressed.remove(event.getCode());
                interpretKeysPressed();
            }
        });
        controlPane.tfControlField.setOnMousePressed(event -> {
            if (!mouseButtonsPressed.contains(event.getButton()))
                mouseButtonsPressed.add(event.getButton());
            handleMouseCombo();
        });
        controlPane.tfControlField.setOnMouseReleased(event -> {
            if (mouseButtonsPressed.contains(event.getButton()))
                mouseButtonsPressed.remove(event.getButton());
            handleMouseCombo();
        });
        controlPane.tfControlField.setOnScroll(event -> {
            if (speedNotIgnored(true) && speedNotIgnored(false)) {
                handleScroll(event);
            }
        });
    }

    public void interpretKeysPressed() {
        synchronized (this) {
            if (keysPressed.contains(LEFT_STATE_KEYS[0])
                    && !keysPressed.contains(LEFT_STATE_KEYS[1])
                    && !keysPressed.contains(LEFT_STATE_KEYS[2]))
                robot.setLeftMotor(MotorState.CCLOCKWISE);

            else if (!keysPressed.contains(LEFT_STATE_KEYS[0])
                    && keysPressed.contains(LEFT_STATE_KEYS[1])
                    && !keysPressed.contains(LEFT_STATE_KEYS[2]))
                robot.setLeftMotor(MotorState.BRAKE);

            else if (!keysPressed.contains(LEFT_STATE_KEYS[0])
                    && !keysPressed.contains(LEFT_STATE_KEYS[1])
                    && keysPressed.contains(LEFT_STATE_KEYS[2]))
                robot.setLeftMotor(MotorState.CLOCKWISE);
            else
                robot.setLeftMotor(MotorState.COAST);

            if (keysPressed.contains(RIGHT_STATE_KEYS[0])
                    && !keysPressed.contains(RIGHT_STATE_KEYS[1])
                    && !keysPressed.contains(RIGHT_STATE_KEYS[2]))
                robot.setRightMotor(MotorState.CCLOCKWISE);

            else if (!keysPressed.contains(RIGHT_STATE_KEYS[0])
                    && keysPressed.contains(RIGHT_STATE_KEYS[1])
                    && !keysPressed.contains(RIGHT_STATE_KEYS[2]))
                robot.setRightMotor(MotorState.BRAKE);

            else if (!keysPressed.contains(RIGHT_STATE_KEYS[0])
                    && !keysPressed.contains(RIGHT_STATE_KEYS[1])
                    && keysPressed.contains(RIGHT_STATE_KEYS[2]))
                robot.setRightMotor(MotorState.CLOCKWISE);
            else
                robot.setRightMotor(MotorState.COAST);
        }
        robot.tick();
    }

    public void handleMouseCombo() {
        synchronized (this) {
            curMouseCombo = MouseCombo.getByCombo(
                    mouseButtonsPressed.toArray(new MouseButton[mouseButtonsPressed.size()])
            );
            if (curMouseCombo != null) {
                switch (curMouseCombo) {
                    case EQUALIZE_SPDS:
                        if (speedNotIgnored(true) && speedNotIgnored(false)) {
                            int speed = (robot.getLeftSpeed() + robot.getRightSpeed()) / 2;
                            robot.setLeftSpeed(speed);
                            robot.setRightSpeed(speed);
                        }
                        break;
                    case SYNC_SPDS_TO_LEFT:
                        if (speedNotIgnored(true) && speedNotIgnored(false))
                            robot.setRightSpeed(robot.getLeftSpeed());
                        break;
                    case SYNC_SPDS_TO_RIGHT:
                        if (speedNotIgnored(true) && speedNotIgnored(false))
                            robot.setLeftSpeed(robot.getRightSpeed());
                        break;
                }
            }
        }
    }

    public void handleScroll(ScrollEvent scrollEvent) {
        if (curMouseCombo == null) {
            int leftSpeed = robot.getLeftSpeed() + (int) scrollEvent.getDeltaY()/8;
            int rightSpeed = robot.getRightSpeed() + (int) scrollEvent.getDeltaY()/8;
            if (scrollEvent.getDeltaY() < 0) {
                robot.setLeftSpeed((leftSpeed > 0xFE ? 0xFE : leftSpeed));
                robot.setRightSpeed(rightSpeed > 0xFE ? 0xFE : rightSpeed);
            } else {
                robot.setLeftSpeed((leftSpeed < 0 ? 0 : leftSpeed));
                robot.setRightSpeed(rightSpeed < 0 ? 0 : rightSpeed);
            }
        } else switch (curMouseCombo) {
            case LEFT_SPD_SET:
                robot.setLeftSpeed(robot.getLeftSpeed() + (int) scrollEvent.getTotalDeltaY());
                break;
            case RIGHT_SPD_SET:
                robot.setRightSpeed(robot.getRightSpeed() + (int) scrollEvent.getTotalDeltaY());
                break;
            case SYNC_SPDS_TO_RIGHT:
                break;
            case SYNC_SPDS_TO_LEFT:
                break;
            case EQUALIZE_SPDS:
                break;
        }
        robot.tick();
    }

    public void actionBTStart() {
        robot.on();
        controlPane.tfControlField.setDisable(false);
    }

    public void actionBTStop() {
        robot.tick();
        robot.off();
        controlPane.tfControlField.setDisable(true);
    }

    private boolean speedNotIgnored(boolean left) {
        /*
        if (left) return
                robot.getLeftMotor() == MotorState.CLOCKWISE
                        || robot.getLeftMotor() == MotorState.CCLOCKWISE;
        else return
                robot.getRightMotor() == MotorState.CLOCKWISE
                        || robot.getRightMotor() == MotorState.CCLOCKWISE;
                        */
        return true;
    }
}
