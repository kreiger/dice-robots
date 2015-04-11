package com.linuxgods.dice.robots;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Game {

    public void mainLoop(State state, State.Listener listener, Stream<Direction> directions) {
        directions.reduce(state, updateState().andThen(notify(listener)), (b1, b2) -> b1);
    }

    private Function<State, State> notify(State.Listener listener) {
        return peek(listener::notify);
    }

    private static <T> Function<T,T> peek(Consumer<T> consumer) {
        return input -> {
            consumer.accept(input);
            return input;
        };
    }

    private BiFunction<State, Direction, State> updateState() {
        return new Logic()::update;
    }

    public static class State {
        private final Optional<Board> board;
        private final Phase phase;
        private final int level;

        public State(Optional<Board> board, Phase phase, int level) {
            this.board = board;
            this.phase = phase;
            this.level = level;
        }

        public Optional<Board> getBoard() {
            return board;
        }

        public Phase getPhase() {
            return phase;
        }

        public int getLevel() {
            return level;
        }

        interface Listener {
            void notify(State state);
        }

    }

    public enum Phase {
        START,
        PLAYING,
        LOST,
        WON
    }
}
