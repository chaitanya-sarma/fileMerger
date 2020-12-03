package com.cerebra.fileMerger.ui.mainFrame;

import com.cerebra.fileMerger.util.SharedInformation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public abstract class TemplateSelectionPanel extends JPanel {
    public JTextField inputPath, outputPath;
    JButton inputBrowse, outputBrowse, mergeBtn;
    SharedInformation sharedInformation;
    int width = 600, height = 200, xPos, yPos;

    TemplateSelectionPanel(SharedInformation sharedInformation, int xPos, int yPos, String title) {
        this.sharedInformation = sharedInformation;
        this.xPos = xPos;
        this.yPos = yPos;
        setBorder(new LineBorder(Color.WHITE));
        setOpaque(true);
        setBackground(new Color(187, 186, 186, 226));
        setBounds(xPos, yPos, width, height);
        setLayout(null);

        yPos = 10;
        JLabel fileMergerLbl = new JLabel(title);
        fileMergerLbl.setForeground(new Color(246, 129, 29));
        fileMergerLbl.setHorizontalAlignment(SwingConstants.CENTER);
        fileMergerLbl.setFont(new Font("Tahoma", Font.PLAIN, 25));
        fileMergerLbl.setBounds((width - 200) / 2, yPos, 200, 30);
        add(fileMergerLbl);
        yPos += 30;


        JLabel inputIconLbl = new JLabel("");
        inputIconLbl.setIcon(new ImageIcon(CSVSelectionPanel.class.getResource("/input.png")));
        inputIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        inputIconLbl.setBounds(70, yPos, 40, 40);
        add(inputIconLbl);
        yPos += 40;
        JLabel inputLbl = new JLabel("Input");
        inputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        inputLbl.setForeground(new Color(255, 255, 255));
        inputLbl.setBounds(65, yPos, 50, 20);
        inputLbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
        add(inputLbl);
        yPos += 20;

        inputPath = new JTextField(20);
        inputPath.setBounds(130, yPos - 25 - 15, 300, 25);
        inputPath.setEditable(false);
        add(inputPath);

        inputBrowse = new JButton("Browse");
        inputBrowse.addActionListener(this::inputBrowseAction);
        inputBrowse.setBounds(450, yPos - 25 - 15, 80, 25);
        add(inputBrowse);

        JLabel outputIconLbl = new JLabel("");
        outputIconLbl.setToolTipText("Output");
        outputIconLbl.setIcon(new ImageIcon(CSVSelectionPanel.class.getResource("/output.png")));
        outputIconLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputIconLbl.setBounds(70, yPos, 40, 40);
        yPos += 40;
        add(outputIconLbl);

        JLabel outputLbl = new JLabel("Output");
        outputLbl.setHorizontalAlignment(SwingConstants.CENTER);
        outputLbl.setForeground(new Color(255, 255, 255));
        outputLbl.setBounds(64, yPos, 55, 20);
        outputLbl.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(outputLbl);
        yPos += 20;

        outputPath = new JTextField(20);
        outputPath.setBounds(130, yPos - 25 - 15, 300, 25);
        outputPath.setEditable(false);
        add(outputPath);

        outputBrowse = new JButton("Browse");
        outputBrowse.addActionListener(this::outputBrowseAction);
        outputBrowse.setBounds(450, yPos - 25 - 15, 80, 25);
        add(outputBrowse);

        mergeBtn = new JButton("Merge");
        mergeBtn.addActionListener(this::mergeAction);
        mergeBtn.setBounds((width - 80) / 2, yPos + 5, 80, 30);
        add(mergeBtn);
    }

    protected void buttonEnabled(boolean b) {
        inputBrowse.setEnabled(b);
        outputBrowse.setEnabled(b);
        mergeBtn.setEnabled(b);
    }

    abstract void mergeAction(ActionEvent actionEvent);

    abstract void outputBrowseAction(ActionEvent actionEvent);

    abstract void inputBrowseAction(ActionEvent actionEvent);
}
