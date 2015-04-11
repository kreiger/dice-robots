package com.linuxgods.dice.robots;

import com.linuxgods.dice.robots.Board.Position;
import com.linuxgods.dice.robots.Board.TileContent;

import java.util.Optional;

import static com.linuxgods.dice.robots.Board.TileContent.*;
import static com.linuxgods.dice.robots.GameState.*;

class Logic {
    private Board board;

    public Logic(Board board) {
        this.board = board;
    }

    public GameState update(Direction direction) {
        boolean dead = movePlayer(direction);
        if (dead) {
            return GAME_OVER;
        }
        dead = moveRobots();
        if (dead) {
            return GAME_OVER;
        }
        scrapRobots();
        return GameState.RUNNING;
    }

    private boolean movePlayer(Direction direction) {
        final Position initialPlayerPosition = board.getPlayerPosition();
        final Position newPosition = initialPlayerPosition.move(direction);
        final Optional<TileContent> contentOnNewPosition = board.getTileContent(newPosition);
        boolean dead = contentOnNewPosition
                .filter(tile -> (tile == ALIEN || tile == PILE))
                .isPresent();
        if (dead) {
            return true;
        } else {
            moveTile(initialPlayerPosition, newPosition);
            return false;
        }
    }

    private void moveTile(Position from, Position to) {
        board.getTileContent(from).ifPresent(tile -> {
            board.clearTile(from);
            board.setTileContent(to, tile);
        });
    }

    private boolean moveRobots() {
        board.getCoordinatesFor(ALIEN).forEach(initialRobotPosition -> {
            final Direction directionToPlayer = getDirectionToPlayer(initialRobotPosition);
            Position newRobotPosition = initialRobotPosition.move(directionToPlayer);
            moveTile(initialRobotPosition, newRobotPosition);
        });
        return false;
    }

    private void scrapRobots() {

    }

    private Direction getDirectionToPlayer(Position robotPosition) {
        return robotPosition.getDirectionTo(board.getPlayerPosition());
    }
}
