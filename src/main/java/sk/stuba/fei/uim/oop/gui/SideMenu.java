package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.GameLogic;
import sk.stuba.fei.uim.oop.controls.RestartButton;
import sk.stuba.fei.uim.oop.controls.SizeSlider;

import javax.swing.*;
import java.awt.*;

public class SideMenu extends JPanel {

    public SideMenu(RestartButton buttonRestart, SizeSlider slider, GameLogic logic) {
        super();
        this.setLayout(new GridLayout(2, 2));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        this.add(buttonRestart);
        this.add(slider);
        this.add(logic.getBoardSizeLabel());
        this.add(logic.getWinsLabel());
    }

}
