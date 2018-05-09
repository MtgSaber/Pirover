package net.mtgsaber.pirover.chassiscontrol4;

/**
 * Author: Andrew Arnold (3/24/2018)
 */
public class Test {
    public static void main(String[] args) {
        Robot robot;
        try {
            System.out.println("Instantiating Robot...");
            robot = new Robot();
            pause(500);
            System.out.println("Starting Robot connection...");
            robot.start();
            pause(500);
            System.out.println("Setting Robot states...");
            robot.setStates(MotorState.CLOCKWISE, MotorState.CCLOCKWISE, 0x7E, 0x7E, 0x0, 0x0, 0x0, 0x0);
            pause(500);
            System.out.print("Robot command is:\n\t");
            for (char c : robot.getStatus().toCharArray())
                System.out.print(("\t" + (int) c));
            System.out.println("\nRobot state string: \"" + robot.getStatus() + "\"");
            System.out.println("Writing Robot states to Arduino...");
            robot.writeStates();
            pause(5000);
        } catch (Exception ex) {
            System.out.println("Mission Failed!");
            ex.printStackTrace();
        }
    }

    public static void pause(long millis) {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + millis;
        while (System.currentTimeMillis() < endTime) ;
    }
}
