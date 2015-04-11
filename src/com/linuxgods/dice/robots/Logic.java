package com.linuxgods.dice.robots;

import com.linuxgods.dice.robots.Board.Position;
import com.linuxgods.dice.robots.Board.TileContent;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.linuxgods.dice.robots.Board.TileContent.*;

class Logic {

    public Board update(Board initialBoard, Direction direction) {
        final Position newPlayerPosition = initialBoard.movePlayer(direction);
        final Optional<TileContent> contentOnNewPosition = initialBoard.getTileContent(newPlayerPosition);
        boolean dead = contentOnNewPosition
                .filter(TileContent::isDeadly)
                .isPresent();

        final BoardBuilder boardBuilder = new BoardBuilder()
                .copyAllPiles(initialBoard);
        if (dead) {
            boardBuilder
                    .copyAllAliens(initialBoard)
                    .setTombstone(newPlayerPosition);
        } else {
            boardBuilder.setPlayerPosition(newPlayerPosition);
            moveAliens(newPlayerPosition, initialBoard, boardBuilder);
        }
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
