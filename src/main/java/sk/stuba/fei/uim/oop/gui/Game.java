package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {
    public Game() {
        JFrame frame = new JFrame("Waterpipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960,720);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setLocationRelativeTo(null);

        GameLogic logic = new GameLogic(frame);
        frame.addKeyListener(logic);

        JButton buttonRestart = new JButton("RESTART");
        buttonRestart.setFocusable(false);
        buttonRestart.addActionListener(logic);

        JPanel sideMenu = new JPanel();
        sideMenu.setLayout(new GridLayout(2, 2));
        sideMenu.setBackground(Color.WHITE);
        sideMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 12, 8);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(logic);

        sideMenu.add(buttonRestart);
        sideMenu.add(slider);
        sideMenu.add(logic.getBoardSizeLabel());
        frame.add(sideMenu, BorderLayout.SOUTH);

        frame.setVisible(true);
    }
}
