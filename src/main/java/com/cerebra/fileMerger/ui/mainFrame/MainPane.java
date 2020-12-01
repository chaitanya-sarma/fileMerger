/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cerebra.fileMerger.ui.mainFrame;

import com.cerebra.fileMerger.ui.AboutDialog;
import com.cerebra.fileMerger.util.SharedInformation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author Chaitanya
 */
@org.springframework.stereotype.Component
public class MainPane extends JLayeredPane {
    SharedInformation sharedInformation;
    JPanel homePanel, csvPanel, tsvPanel, tipsPanel;
    JTextField home, csv, tsv, about;
    AboutDialog aboutDialog;

    @Autowired
    public void setAboutDialog(AboutDialog aboutDialog) {
        this.aboutDialog = aboutDialog;
    }

    public JLayeredPane createHomeTab(SharedInformation sharedInformation) {
        this.sharedInformation = sharedInformation;
        setOpaque(false);
        setBackground(new Color(187, 186, 186, 226));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(null);

        int yPos = 0;
        int xPos = 0;
        add(createTopBar(xPos, yPos, sharedInformation.getXSize(), 50));
        yPos = 50;
        homePanel = new HomePanel().createHomePanel(sharedInformation, xPos + 200, yPos, 600, 400);
        csvPanel = new CSVSelectionPanel(sharedInformation, 200, yPos);
        tsvPanel = new TSVSelectionPanel().createTSVSelectionPanel(sharedInformation, 200, yPos, 600, 200);
        tipsPanel = new TipsPanel().createTipsPanel(200, yPos + 200, 600, 190);
        add(createSidePanel(0, yPos - 10, 200, 500));
        add(homePanel);
        return this;
    }

    private JPanel createSidePanel(int xPos, int yPos, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBorder(null);
        panel.setBackground(new Color(246, 129, 29));
        panel.setBounds(xPos, yPos, width, height);
        panel.setLayout(null);

        home = new JTextField();
        home.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                resetComponents(home);
                add(homePanel, JLayeredPane.DRAG_LAYER);
                repaint();
            }
        });
        setProperties(panel, home, "Home", 20);

        csv = new JTextField();
        csv.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                resetComponents(csv);
                add(csvPanel, JLayeredPane.DRAG_LAYER);
                add(tipsPanel, JLayeredPane.DEFAULT_LAYER);
                repaint();
            }
        });
        setProperties(panel, csv, "CSV", 50);

        tsv = new JTextField();
        tsv.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                resetComponents(tsv);
                add(tsvPanel, JLayeredPane.DRAG_LAYER);
                add(tipsPanel, JLayeredPane.DEFAULT_LAYER);
                repaint();
            }
        });
        setProperties(panel, tsv, "TSV", 80);

        about = new JTextField();
        about.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                aboutDialog.setVisible(true);
            }
        });
        setProperties(panel, about, "About", 110);

        JTextField credits = new JTextField();
        setProperties(panel, credits, "Credits", 300);
        credits.setFont(new Font("Tahoma", Font.PLAIN, 15));
        credits.setBounds(20, 300, 150, 25);
        credits.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField copyRight = new JTextField();
        setProperties(panel, copyRight, "All copyrights @cerebra-consulting", 320);
        copyRight.setBounds(10, 320, 180, 25);
        copyRight.setFont(new Font("Tahoma", Font.PLAIN, 10));
        copyRight.setHorizontalAlignment(SwingConstants.CENTER);
        return panel;
    }

    private void resetComponents(JTextField textField) {
        sharedInformation.setInputFiles(new ArrayList<>());
        sharedInformation.setOutputFolder(null);
        home.setForeground(new Color(240, 248, 255));
        csv.setForeground(new Color(240, 248, 255));
        tsv.setForeground(new Color(240, 248, 255));
        remove(homePanel);
        remove(csvPanel);
        remove(tsvPanel);
        remove(tipsPanel);
        csvPanel = new CSVSelectionPanel(sharedInformation, 200, 50);
        tsvPanel = new TSVSelectionPanel().createTSVSelectionPanel(sharedInformation, 200, 50, 600, 200);
        textField.setForeground(new Color(0, 112, 170));
    }

    private void setProperties(JPanel panel, JTextField field, String name, int yPos) {
        field.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        field.setFocusable(false);
        field.setEditable(false);
        field.setForeground(new Color(240, 248, 255));
        field.setText(name);
        field.setHorizontalAlignment(SwingConstants.LEFT);
        field.setFont(new Font("Tahoma", Font.PLAIN, 20));
        field.setColumns(10);
        field.setBorder(null);
        field.setBackground(new Color(246, 129, 29));
        field.setBounds(50, yPos, 150, 25);
        panel.add(field);
    }


    private JPanel createTopBar(int xPos, int yPos, int width, int height) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.setBounds(xPos, yPos, width, height);
        panel.setLayout(null);

        JLabel inputLabel = new JLabel("");
        inputLabel.setHorizontalAlignment(SwingConstants.LEFT);
        inputLabel.setIcon(new ImageIcon(MainPane.class.getResource("/logo.png")));
        inputLabel.setBounds(0, 0, 300, 50);
        panel.add(inputLabel);

        JLabel user_name = new JLabel("Cerebra Consulting Inc");
        user_name.setHorizontalAlignment(SwingConstants.CENTER);
        user_name.setForeground(new Color(112, 128, 144));
        user_name.setFont(new Font("Tahoma", Font.BOLD, 20));
        user_name.setBounds(350, 10, 300, 25);
        panel.add(user_name);
        return panel;
    }
}
