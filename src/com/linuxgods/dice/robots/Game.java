package com.linuxgods.dice.robots;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import static com.linuxgods.dice.robots.Board.Position.pos;
import static com.linuxgods.dice.robots.Board.TileContent.PLAYER;

public class Game {

    private Board initBoard() {
        BoardBuilder boardBuilder = new BoardBuilder()
                .setPlayerPosition(pos(10, 10));
        IntStream.range(0, 10)
                .forEach(i -> boardBuilder.placeRandomAlien());
        return boardBuilder.build();
    }

    public void mainLoop(BlockingQueue<Integer> keyCodeQueue, Listener listener) {
        Board initBoard = initBoard();
        Logic logic = new Logic();
        listener.boardUpdated(initBoard);
        getKeyCodes(keyCodeQueue)
                .mapToObj(Direction::forKeyCode)
                .filter(Optional::isPresent).map(Optional::get)
                .peek(System.out::println)
                .reduce(initBoard, (initialBoard, direction) -> {
                    final Board updatedBoard = logic.update(initialBoard, direction);
                    listener.boardUpdated(updatedBoard);
                    return updatedBoard;
                }, (b1, b2) -> (b1));
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

    public interface Listener {
        void boardUpdated(Board board);
    }
}
