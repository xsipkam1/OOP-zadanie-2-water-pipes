package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;
import sk.stuba.fei.uim.oop.controls.MenuInput;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JPanel {

    public SideMenu(GameLogic baseLogic) {
        this.setLayout(new GridLayout(2, 1));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        JButton buttonRestart = new JButton("RESTART");
        buttonRestart.setFocusable(false);

        JButton buttonCheck = new JButton("CHECK");
        buttonCheck.setFocusable(false);

        MenuInput userMenuInputManager = new MenuInput(baseLogic, buttonRestart, buttonCheck);
        buttonRestart.addActionListener(userMenuInputManager);
        buttonCheck.addActionListener(userMenuInputManager);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, 8, 10, 8);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(userMenuInputManager);

        JPanel upperPart = new JPanel(new GridLayout(1, 2));
        upperPart.add(buttonRestart);
        upperPart.add(buttonCheck);

        JPanel lowerPart = new JPanel(new GridLayout(1, 3));
        lowerPart.add(baseLogic.getLevelLabel());
        lowerPart.add(slider);
        lowerPart.add(baseLogic.getBoardSizeLabel());

        this.add(upperPart);
        this.add(lowerPart);
    }

}
