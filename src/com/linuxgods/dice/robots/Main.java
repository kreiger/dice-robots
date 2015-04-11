package com.linuxgods.dice.robots;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.IntStream;

import static com.linuxgods.dice.robots.Board.Position.pos;
import static com.linuxgods.dice.robots.Board.TileContent.PLAYER;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        board.setTileContent(pos(50, 50), PLAYER);
        IntStream.range(0, 10)
                .forEach(i -> board.setTileContent(board.randomCoordinate(), Board.TileContent.ROBOT));
        BoardGraphicsComponent boardGraphicsComponent = new BoardGraphicsComponent(board);
        JFrame jFrame = new MainFrame(boardGraphicsComponent);
        jFrame.setVisible(true);

        Queue<Integer> inputQueue = new LinkedList<>();
        jFrame.addKeyListener(new KeyListener(inputQueue));

        Logic logic = new Logic(board);
        while (true) {
            int nextKeyCode = getNextKeyCode(inputQueue);
            Direction.forKeyCode(nextKeyCode)
                    .ifPresent(direction -> {
                        System.out.println(direction);
                        logic.update(direction);
                        boardGraphicsComponent.repaint();
                    });
        }
    }

    private static int getNextKeyCode(Queue<Integer> inputQueue) {

        while (true) {
            Optional<Integer> optionalKeyCode = Optional.ofNullable(inputQueue.poll());
            if (optionalKeyCode.isPresent()) {
                return optionalKeyCode.get();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }
    }

    private static class KeyListener implements java.awt.event.KeyListener {
        private final Queue<Integer> inputQueue;

        public KeyListener(Queue<Integer> inputQueue) {
            this.inputQueue = inputQueue;
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            inputQueue.add(e.getKeyCode());
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }
}
