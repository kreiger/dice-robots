package com.linuxgods.dice.robots;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        BoardGraphicsComponent boardGraphicsComponent = new BoardGraphicsComponent();

        getContentPane().add(boardGraphicsComponent);
        pack();

        setVisible(true);
    }
}
