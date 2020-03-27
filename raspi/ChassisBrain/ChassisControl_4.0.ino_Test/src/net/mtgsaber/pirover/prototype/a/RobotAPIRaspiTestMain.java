package net.mtgsaber.pirover.prototype.a;

/**
 * Author: Andrew Arnold (4/25/2018)
 */
/*
public class RobotAPIRaspiTestMain {
    public static void main(String[] args) {
        if (args.length != 4)
            System.out.println("Invalid number of args:\t" + args.length);
        else {
            Integer[] argsInts = new Integer[2];

            boolean succ1 = false;
            try {
                if (MotorState.getByName(args[0]) == null) throw new Exception("Bad arg0:\t" + args[0]);
                if (MotorState.getByName(args[1]) == null) throw new Exception("Bad arg1:\t" + args[1]);
                argsInts[0] = Integer.parseInt(args[2]);
                argsInts[1] = Integer.parseInt(args[3]);
                succ1 = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!succ1)
                System.out.println(
                        "Usage:\n"
                                + "arg0:\t" + "Left Motor State"
                                + "arg1:\t" + "Right Motor State"
                                + "arg2:\t" + "Left Motor Speed"
                                + "arg3:\t" + "Right Motor Speed"
                );
            else {
                try {
                    System.out.println("Initializing Robot...");
                    Robot robot = new Robot();
                    System.out.println("Starting Robot...");
                    robot.start();
                    System.out.println("Writing Robot States...");
                    robot.setStates(
                            MotorState.getByName(args[0]),
                            MotorState.getByName(args[1]),
                            argsInts[0],
                            argsInts[1],
                            0, 0, 0, 0
                    );
                    System.out.println("Realizing Robot States...");
                    robot.writeStates();
                    System.out.println("Shutting Off Robot...");
                    robot.end();
                    System.out.println("End Robot API Test.\n");
                } catch (AlreadyInUseException aiuex) {
                    aiuex.printStackTrace();
                } catch (ConnectionFailedException cfex) {
                    cfex.printStackTrace();
                }
            }
        }
    }
}
*/
