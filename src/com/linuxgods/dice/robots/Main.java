package com.linuxgods.dice.robots;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        board.addPlayer(50,50);
        JFrame jFrame = new MainFrame(new BoardGraphicsComponent(board));
        jFrame.setVisible(true);
    }


}
