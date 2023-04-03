package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;

import javax.swing.*;
import java.awt.*;

public class Game {
    private JFrame frame;
    private GameLogic logic;
    private JButton buttonRestart;
    private JPanel sideMenu;
    private JSlider slider;

    public Game() {
        initializeButton();
        initializeSlider();
        initializeSideMenu();
        initializeFrame();
        addListeners();
    }

    private void initializeFrame() {
        frame = new JFrame("Waterpipes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960,720);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setLocationRelativeTo(null);
        frame.add(sideMenu, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void initializeButton() {
        buttonRestart = new JButton("RESTART");
        buttonRestart.setFocusable(false);
    }

    private void initializeSlider() {
        slider = new JSlider(JSlider.HORIZONTAL, 8, 12, 8);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(2);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
    }

    private void initializeSideMenu() {
        sideMenu = new JPanel();
        sideMenu.setLayout(new GridLayout(2, 2));
        sideMenu.setBackground(Color.WHITE);
        sideMenu.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        sideMenu.add(buttonRestart);
        sideMenu.add(slider);
    }

    private void addListeners() {
        logic = new GameLogic(frame);
        frame.addKeyListener(logic);
        slider.addChangeListener(logic);
        buttonRestart.addActionListener(logic);
        sideMenu.add(logic.getBoardSizeLabel());
    }

}
