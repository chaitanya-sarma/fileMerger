package com.cerebra.fileMerger.ui;

import com.cerebra.fileMerger.util.Util;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * About panel.
 */
@Component
public class AboutDialog {
    private JDialog dialogBox;

    public void createDialog(JFrame mainFrame) {
        dialogBox = new JDialog(mainFrame, Dialog.ModalityType.APPLICATION_MODAL);
        dialogBox.setSize(500, 250);
        dialogBox.setLocation(Util.getLocation(500, 250));
        dialogBox.setTitle("About");
        JLabel background = new JLabel();
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/FileMerger.JPG"));
        background.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(500,250,Image.SCALE_DEFAULT)));
        dialogBox.add(background);
        dialogBox.setResizable(false);

        dialogBox.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                dialogBox.setVisible(false);
            }

            public void windowClosing(WindowEvent e) {
                dialogBox.setVisible(false);
            }
        });
    }

    public void setVisible(boolean visible) {
        dialogBox.setVisible(visible);
    }
}
