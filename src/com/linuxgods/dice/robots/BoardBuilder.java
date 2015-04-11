package com.linuxgods.dice.robots;

import com.linuxgods.dice.robots.Board.Position;

import java.util.Optional;

import static com.linuxgods.dice.robots.Board.TileContent.*;

public class BoardBuilder {
    private final Board board;

    public BoardBuilder() {
        board = new Board();
    }

    public BoardBuilder setPlayerPosition(Position position) {
        board.setTileContent(position, PLAYER);
        return this;
    }


    public BoardBuilder setTombstone(Position position) {
        board.setTileContent(position, TOMBSTONE);
        return this;
    }

    public BoardBuilder setAlienPosition(Position position) {
        Optional<Board.TileContent> tileContent = board.getTileContent(position);
        if (tileContent.isPresent()) {
            switch (tileContent.get()) {
                case ALIEN:
                    setPile(position);
                    break;
                case PLAYER:
                    setTombstone(position);
                    break;
                default:
            }
        } else {
            board.setTileContent(position, ALIEN);
        }
        return this;
    }

    private BoardBuilder setPile(Position position) {
        board.setTileContent(position, PILE);
        return this;
    }

    public BoardBuilder copyAllAliens(Board referenceBoard) {
        referenceBoard.getPositionsFor(ALIEN).forEach(alienPosition ->
                board.setTileContent(alienPosition, ALIEN));
        return this;
    }

    public BoardBuilder copyAllPiles(Board referenceBoard) {
        referenceBoard.getPositionsFor(PILE).forEach(alienPosition ->
                board.setTileContent(alienPosition, PILE));
        return this;
    }

    public void placeRandomAlien() {
        board.setTileContent(board.randomEmptyPosition(), ALIEN);
    }

    public Board build() {
        return board;
    }
}
