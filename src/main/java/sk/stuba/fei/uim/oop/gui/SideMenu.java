package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.CheckButton;
import sk.stuba.fei.uim.oop.controls.GameLogic;
import sk.stuba.fei.uim.oop.controls.RestartButton;
import sk.stuba.fei.uim.oop.controls.SizeSlider;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JPanel {

    public SideMenu(GameLogic logic, RestartButton buttonRestart, CheckButton buttonCheck, SizeSlider slider) {
        super();
        this.setLayout(new GridLayout(2, 1));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        JPanel upperPart = new JPanel(new GridLayout(1, 2));
        upperPart.add(buttonRestart);
        upperPart.add(buttonCheck);

        JPanel lowerPart = new JPanel(new GridLayout(1, 3));
        lowerPart.add(logic.getWinsLabel());
        lowerPart.add(slider);
        lowerPart.add(logic.getBoardSizeLabel());

        this.add(upperPart);
        this.add(lowerPart);
    }

}
