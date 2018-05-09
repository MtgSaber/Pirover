package net.mtgsaber.pirover.prototype.a.swing;

import javax.swing.*;

/**
 * Author: Andrew Arnold (4/30/2018)
 */
public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PiroverControlPane piroverControlPane = new PiroverControlPane();
                JFrame jFrame = new JFrame();
                jFrame.getContentPane().add(piroverControlPane.getPanel1());
                jFrame.pack();
                jFrame.setVisible(true);
            }
        });
    }
}
