package com.cerebra.fileMerger.ui.mainFrame;

import com.cerebra.fileMerger.util.SharedInformation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class HomePanel extends JPanel {
    SharedInformation sharedInformation;

    public void paintComponent(Graphics g) {
        Image image = new ImageIcon(HomePanel.class.getResource("/home.jpg")).getImage();
        g.drawImage(image, 0, 0, null);
    }

    JPanel createHomePanel1(SharedInformation sharedInformation, int xPos, int yPos, int width, int height) {

        return this;
    }

    JPanel createHomePanel(SharedInformation sharedInformation, int xPos, int yPos, int width, int height) {
        this.sharedInformation = sharedInformation;
        setBorder(new LineBorder(Color.WHITE));
        setOpaque(true);
        setBounds(xPos, yPos, width, height);
        setLayout(null);

        yPos = 10;
        JLabel fileMergerLbl = new JLabel("Home");
        fileMergerLbl.setForeground(new Color(246, 129, 29));
        fileMergerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileMergerLbl.setFont(new Font("Tahoma", Font.PLAIN, 25));
        fileMergerLbl.setBounds((width - 200) / 2, yPos, 200, 30);
        add(fileMergerLbl);

        yPos += 40;
        JLabel text = new JLabel("<html><div style='text-align: center;'>This application supports merging of files.</div></html>");
        text.setForeground(Color.WHITE);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setFont(new Font("Tahoma", Font.PLAIN, 18));
        text.setBounds(10, yPos, width - 30, 50);
        add(text);

        yPos += 50;
        JLabel features = new JLabel("Features:");
        features.setForeground(Color.WHITE);
        features.setHorizontalAlignment(SwingConstants.CENTER);
        features.setFont(new Font("Tahoma", Font.PLAIN, 20));
        features.setBounds(10, yPos, 100, 25);
        add(features);

        yPos += 25;
        JLabel csv = new JLabel("->Merge CSV files.");
        csv.setForeground(Color.WHITE);
        csv.setHorizontalAlignment(SwingConstants.CENTER);
        csv.setFont(new Font("Tahoma", Font.PLAIN, 15));
        csv.setBounds(15, yPos, 150, 25);
        add(csv);

        yPos += 25;
        JLabel tsv = new JLabel("->Merge TSV files.");
        tsv.setForeground(Color.WHITE);
        tsv.setHorizontalAlignment(SwingConstants.CENTER);
        tsv.setFont(new Font("Tahoma", Font.PLAIN, 15));
        tsv.setBounds(15, yPos, 150, 25);
        add(tsv);
        return this;
    }
}
