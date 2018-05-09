package net.mtgsaber.pirover.prototype.a.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Author: Andrew Arnold (4/30/2018)
 */
public class PiroverControlPane {
    private JTextArea textArea1;
    private JPanel panel1;
    private JButton button1;
    private JButton button2;
    private JPanel panel2;
    private MouseAdapter mouseListener;

    public PiroverControlPane() {
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

            }
        };

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        textArea1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });
        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                textArea1.replaceRange("", 0, textArea1.getText().length());
                textArea1.setText("" + e.getKeyChar());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
    }

    public JTextArea getTextArea1() {
        return textArea1;
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public JButton getButton1() {
        return button1;
    }

    public JButton getButton2() {
        return button2;
    }

    public JPanel getPanel2() {
        return panel2;
    }
}
