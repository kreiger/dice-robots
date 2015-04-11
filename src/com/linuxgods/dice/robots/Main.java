package com.linuxgods.dice.robots;

import javax.swing.*;
import java.util.stream.IntStream;

import static com.linuxgods.dice.robots.Board.TileContent.PLAYER;
import static com.linuxgods.dice.robots.Board.Position.pos;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        board.setTileContent(pos(50, 50), PLAYER);
        IntStream.range(0, 10)
                .forEach(i -> board.setTileContent(board.randomCoordinate(), Board.TileContent.ROBOT));
        JFrame jFrame = new MainFrame(new BoardGraphicsComponent(board));
        jFrame.setVisible(true);
    }


}
