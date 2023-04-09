package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        super("WaterPipes");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(850,1000);
        this.setResizable(false);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setLocationRelativeTo(null);

        GameLogic logic = new GameLogic(this);
        RestartButton buttonRestart = new RestartButton(logic);
        SizeSlider slider = new SizeSlider(logic);
        SideMenu sideMenu = new SideMenu(buttonRestart, slider, logic);

        this.add(sideMenu, BorderLayout.SOUTH);
        this.addKeyListener(new Keyboard(logic));
        this.setVisible(true);
    }

}
