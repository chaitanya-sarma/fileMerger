package com.cerebra.fileMerger.ui;

import javax.swing.*;
import java.awt.*;

public class SubmitPanel extends JPanel {

    public JButton submit, cancel;

    public SubmitPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        submit = new JButton("  Ok  ");
        cancel = new JButton("Cancel");
        add(submit);
        add(cancel);
    }
}
