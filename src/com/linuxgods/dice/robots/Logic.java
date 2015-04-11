package com.linuxgods.dice.robots;

import com.linuxgods.dice.robots.Board.Position;
import com.linuxgods.dice.robots.Board.TileContent;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.linuxgods.dice.robots.Board.Position.pos;
import static com.linuxgods.dice.robots.Board.TileContent.ALIEN;

class Logic {

    public static final int ALIENS_PER_LEVEL = 10;

    public Game.State update(Game.State state, Direction direction) {
        switch (state.getPhase()) {
            case WON:
                int nextLevel = state.getLevel() + 1;
                return new Game.State(Optional.of(createBoard(nextLevel)), Game.Phase.PLAYING, nextLevel);
            case LOST:
            case START:
                return new Game.State(Optional.of(createBoard(1)), Game.Phase.PLAYING, 1);
        }
        Board board = state.getBoard().get();
        final Position newPlayerPosition = board.movePlayer(direction);
        final Optional<TileContent> contentOnNewPosition = board.getTileContent(newPlayerPosition);
        boolean dead = contentOnNewPosition
                .filter(TileContent::isDeadly)
                .isPresent();

        final BoardBuilder boardBuilder = new BoardBuilder()
                .copyAllPiles(board);
        if (dead) {
            boardBuilder
                    .copyAllAliens(board)
                    .setTombstone(newPlayerPosition);
        } else {
            boardBuilder.setPlayerPosition(newPlayerPosition);
            moveAliens(newPlayerPosition, board, boardBuilder);
        }
        Board newBoard = boardBuilder.build();

        return new Game.State(Optional.of(newBoard), newBoard.getPhase(), state.getLevel());
    }

    private static Board createBoard(int level) {
        BoardBuilder boardBuilder = new BoardBuilder()
                .setPlayerPosition(pos(10, 10));
        IntStream.range(0, level * ALIENS_PER_LEVEL)
                .forEach(i -> boardBuilder.placeRandomAlien());
        return boardBuilder.build();
    }

    private void moveAliens(Position playerPosition, Board initialBoard, BoardBuilder boardBuilder) {
        initialBoard.getPositionsFor(ALIEN).forEach(initialAlienPosition -> {
            final Direction directionToPlayer = initialAlienPosition.getDirectionTo(playerPosition);
            Position newAlienPosition = initialAlienPosition.move(directionToPlayer);
            boardBuilder.setAlienPosition(newAlienPosition);
        });
    }
}
