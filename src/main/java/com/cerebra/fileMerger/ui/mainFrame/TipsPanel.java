package com.cerebra.fileMerger.ui.mainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class TipsPanel extends JPanel {
    JPanel createTipsPanel(int xPos, int yPos, int width, int height) {
        setBackground(new Color(243, 240, 240));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBounds(xPos, yPos, width, height);
        setLayout(null);

        yPos = 10;
        JLabel tipsLbl = new JLabel("Tips");
        tipsLbl.setForeground(new Color(128, 128, 128));
        tipsLbl.setHorizontalAlignment(SwingConstants.CENTER);
        tipsLbl.setFont(new Font("Tahoma", Font.BOLD, 25));
        tipsLbl.setBounds(10, yPos, 100, 30);
        yPos += 40;
        add(tipsLbl);

        JLabel inputLabel = new JLabel("");
        inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inputLabel.setIcon(new ImageIcon(TipsPanel.class.getResource("/input.png")));
        inputLabel.setBounds(100, yPos, 200, 40);
        add(inputLabel);

        JLabel outputLabel = new JLabel("");
        outputLabel.setHorizontalAlignment(SwingConstants.CENTER);
        outputLabel.setIcon(new ImageIcon(TipsPanel.class.getResource("/output.png")));
        outputLabel.setBounds(300, yPos, 200, 40);
        add(outputLabel);
        yPos += 40;

        JLabel inputText = new JLabel("<html><div style='text-align: center;'>Click on the 'Browse' in input row to Select input folder</div></html>");
        inputText.setForeground(new Color(105, 105, 105));
        inputText.setFont(new Font("Tahoma", Font.PLAIN, 13));
        inputText.setBounds(100, yPos, 200, 40);
        add(inputText);

        JLabel outputText = new JLabel("<html><div style='text-align: center;'>Click on the 'Browse' in output row to Select output folder</div></html>");
        outputText.setForeground(new Color(105, 105, 105));
        outputText.setFont(new Font("Tahoma", Font.PLAIN, 13));
        outputText.setBounds(300, yPos, 200, 40);
        add(outputText);
        return this;
    }

}
