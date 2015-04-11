package com.linuxgods.dice.robots;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(BoardGraphicsComponent boardGraphicsComponent) {
        getContentPane().add(boardGraphicsComponent);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
