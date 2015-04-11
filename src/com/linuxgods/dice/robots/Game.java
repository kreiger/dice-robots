package com.linuxgods.dice.robots;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import static com.linuxgods.dice.robots.Board.Position.pos;
import static com.linuxgods.dice.robots.Board.TileContent.PLAYER;

public class Game {
    private final Board initialBoard;

    public Game() {
        this.initialBoard = initBoard();
    }

    private static Board initBoard() {
        Board board = new Board();
        board.setTileContent(pos(10, 10), PLAYER);
        IntStream.range(0, 10)
                .forEach(i -> board.setTileContent(board.randomCoordinate(), Board.TileContent.ALIEN));
        return board;
    }

    public void mainLoop(BlockingQueue<Integer> keyCodeQueue, Listener listener) {
        Logic logic = new Logic(initialBoard);
        listener.boardUpdated(initialBoard);
        getKeyCodes(keyCodeQueue)
                .mapToObj(Direction::forKeyCode)
                .filter(Optional::isPresent).map(Optional::get)
                .forEach(direction -> {
                    System.out.println(direction);
                    logic.update(direction);
                    listener.boardUpdated(null);
                });
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
