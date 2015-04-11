package com.linuxgods.dice.robots;

import com.linuxgods.dice.robots.Board.Position;
import com.linuxgods.dice.robots.Board.TileContent;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.linuxgods.dice.robots.Board.Position.pos;
import static com.linuxgods.dice.robots.Board.TileContent.ALIEN;
import static com.linuxgods.dice.robots.Board.TileContent.PILE;

class Logic {

    public Game.State update(Game.State state, Direction direction) {
        if (state.phase != Game.Phase.PLAYING) {
            return new Game.State(Optional.of(initialBoard()), Game.Phase.PLAYING);
        }
        Board board = state.board.get();
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

        return new Game.State(Optional.of(newBoard), newBoard.getPhase());
    }

    private static Board initialBoard() {
        BoardBuilder boardBuilder = new BoardBuilder()
                .setPlayerPosition(pos(10, 10));
        IntStream.range(0, 10)
                .forEach(i -> boardBuilder.placeRandomAlien());
        return boardBuilder.build();
    }

    private boolean moveAliens(Position playerPosition, Board initialBoard, BoardBuilder boardBuilder) {
        for (Position initialAlienPosition : initialBoard.getPositionsFor(ALIEN).collect(Collectors.toList())) {
            final Direction directionToPlayer = initialAlienPosition.getDirectionTo(playerPosition);
            Position newAlienPosition = initialAlienPosition.move(directionToPlayer);
            boardBuilder.setAlienPosition(newAlienPosition);
        }
        return false;
    }
}
