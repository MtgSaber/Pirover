package net.mtgsaber.pirover.prototype.a.fx;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Author: Andrew Arnold (4/19/2018)
 */
public class ControlPane extends VBox {
    private LinkedList<Control> controls;
    final Button btStart, btStop;
    final Text txtInstructions, txtStates;
    final TextField tfControlField;

    public ControlPane(int width, int height) {
        super(8);

        btStart = new Button();
        btStop = new Button();

        txtInstructions = new Text();
        txtStates = new Text();

        tfControlField = new TextField();

        controls = new LinkedList<>();
        controls.addAll(Arrays.asList(
                btStart,
                btStop,
                tfControlField
        ));

        build();

        super.setPrefWidth(width);
        super.setPrefHeight(height);
    }

    private void build() {
        btStart.setText("Start");
        btStop.setText("Stop");

        tfControlField.setEditable(false);

        super.getChildren().addAll(
                txtInstructions,
                tfControlField,
                txtStates,
                new HBox(
                        btStart,
                        btStop
                )
        );
    }

    public synchronized void lockControls() {
        for (Control control : controls)
            control.setDisable(true);
    }
    public synchronized void unlockControls() {
        for (Control control : controls)
            control.setDisable(false);
    }
}
