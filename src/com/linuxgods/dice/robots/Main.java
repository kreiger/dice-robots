package com.linuxgods.dice.robots;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();

        Game.State initialState = new Game.State(Optional.<Board>empty(), Game.Phase.START, 1);
        ViewComponent viewComponent = new ViewComponent(initialState);
        JFrame jFrame = new MainFrame(viewComponent);
        BlockingQueue<Integer> keyCodeQueue = new LinkedBlockingQueue<>();
        jFrame.addKeyListener(new KeyListener(keyCodeQueue));
        jFrame.setVisible(true);

        game.mainLoop(initialState, viewComponent, getDirections(keyCodeQueue));
    }

    private static Stream<Direction> getDirections(BlockingQueue<Integer> keyCodeQueue) {
        return getKeyCodes(keyCodeQueue)
                .mapToObj(Direction::forKeyCode)
                .filter(Optional::isPresent).map(Optional::get);
    }


    private static IntStream getKeyCodes(BlockingQueue<Integer> inputQueue) {
        return IntStream.generate(() -> {
            while (true) {
                try {
                    return inputQueue.take();
                } catch (InterruptedException ignored) {
                }
            }
        });
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
