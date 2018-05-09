package net.mtgsaber.pirover.prototype.a.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.mtgsaber.pirover.chassiscontrol4.AlreadyInUseException;
import net.mtgsaber.pirover.chassiscontrol4.ConnectionFailedException;
import net.mtgsaber.pirover.chassiscontrol4.Robot;

/**
 * Author: Andrew Arnold (4/19/2018)
 */
public class Main extends Application {
    private ControlPane controlPane;
    private ControlPaneEvents controlPaneEvents;
    private Thread robotThreadThread;

    public void start(Stage primaryStage) {
        System.out.close();
        controlPane = new ControlPane(200, 200);
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AlreadyInUseException | ConnectionFailedException ex) {
            ex.printStackTrace();
        }

        controlPaneEvents = new ControlPaneEvents(controlPane, robot);

        //controlPaneEvents.robotClock.addTickable(controlPaneEvents.robot);
        //robotThreadThread = new Thread(controlPaneEvents.robotClock);

        controlPaneEvents.hookEvents();
        primaryStage.setScene(new Scene(controlPane));

        //controlPaneEvents.robotClock.start();
        //robotThreadThread.start();

        primaryStage.setOnCloseRequest(event -> {
            //controlPaneEvents.robotClock.stop();
            //robotThreadThread.stop();
            controlPane.btStop.fire();
        });
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        //robotThreadThread.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
