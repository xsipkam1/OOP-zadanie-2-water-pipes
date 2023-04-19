package sk.stuba.fei.uim.oop.gui;

import sk.stuba.fei.uim.oop.controls.*;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        super("WaterPipes");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(875, 1000);
        this.setResizable(false);
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.setLocationRelativeTo(null);

        GameLogic baseLogic = new GameLogic(this);
        FrameInput userFrameInputManager = new FrameInput(baseLogic);
        SideMenu sideMenu = new SideMenu(baseLogic);

        this.add(sideMenu, BorderLayout.SOUTH);
        this.addKeyListener(userFrameInputManager);
        this.setVisible(true);
    }

}
