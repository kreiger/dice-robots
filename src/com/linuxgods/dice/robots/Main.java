package com.linuxgods.dice.robots;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();

        BoardGraphicsComponent boardGraphicsComponent = new BoardGraphicsComponent();
        JFrame jFrame = new MainFrame(boardGraphicsComponent);
        BlockingQueue<Integer> keyCodeQueue = new LinkedBlockingQueue<>();
        jFrame.addKeyListener(new KeyListener(keyCodeQueue));
        jFrame.setVisible(true);

        game.mainLoop(keyCodeQueue, boardGraphicsComponent);
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
