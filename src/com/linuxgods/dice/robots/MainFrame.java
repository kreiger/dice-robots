package com.linuxgods.dice.robots;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(ViewComponent viewComponent) {
        getContentPane().add(viewComponent);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
