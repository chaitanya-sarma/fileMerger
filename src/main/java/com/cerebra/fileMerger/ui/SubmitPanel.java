package com.cerebra.fileMerger.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SubmitPanel extends JPanel {

    public JButton submit, cancel;

    public SubmitPanel(boolean withTips) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        submit = new JButton("  Ok  ");
        cancel = new JButton("Cancel");
        buttonPanel.add(submit);
        buttonPanel.add(cancel);
        add(buttonPanel, BorderLayout.CENTER);

        if (withTips) {
            JPanel tipsPanel = new JPanel();
            tipsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
            tipsPanel.setLayout(new BoxLayout(tipsPanel, BoxLayout.Y_AXIS));

            JLabel tipsLbl = new JLabel("Tips");
            tipsLbl.setForeground(new Color(246, 129, 29));
            tipsLbl.setHorizontalAlignment(SwingConstants.CENTER);
            tipsLbl.setFont(new Font("Tahoma", Font.BOLD, 25));
            tipsPanel.add(tipsLbl);

            JLabel inputText = new JLabel("<html><div style='text-align: center;'>The first file will be used as reference for column names. Use the Up and Down buttons to order the files. </div></html>");
            inputText.setFont(new Font("Tahoma", Font.PLAIN, 13));
            tipsPanel.add(inputText);
            add(tipsPanel, BorderLayout.SOUTH);
        }
    }
}
