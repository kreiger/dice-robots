package com.linuxgods.dice.robots;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Game {

    public void mainLoop(Board board, Listener listener, Stream<Direction> directions) {
        directions.reduce(board, updateBoard().andThen(notify(listener)), (b1, b2) -> b1);
    }

    private Function<Board, Board> notify(Listener listener) {
        return peek(listener::notifyBoardUpdated);
    }

    private static <T> Function<T,T> peek(Consumer<T> consumer) {
        return input -> {
            consumer.accept(input);
            return input;
        };
    }

    private BiFunction<Board, Direction, Board> updateBoard() {
        return new Logic()::update;
    }

    public interface Listener {
        void notifyBoardUpdated(Board board);
    }
}
