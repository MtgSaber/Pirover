package net.mtgsaber.pirover.chassiscontrol4;

/**
 * Author: Andrew Arnold (3/24/2018)
 */
public class Test {
    public static void main(String[] args) {
        final char[] message = new char[] {
                0xFF,
                0x05,
                0x7F, 0xFE,
                0x00, 0x00, 0x00, 0x00,
                0xFF
        };
        Robot robot;
        try {
            System.out.println("Instantiating Robot...");
            robot = new Robot();
            System.out.println("Starting Robot connection...");
            robot.start();
            System.out.println("Setting Robot states...");
            robot.setStates(MotorState.CLOCKWISE, MotorState.CLOCKWISE, 0x7F, 0xFE, 0,0,0,0);
            System.out.println("Writing Robot states to Arduino...");
            robot.writeStates();
        } catch (Exception ex) {
            System.out.println("Mission Failed!");
            ex.printStackTrace();
        }
    }
}
