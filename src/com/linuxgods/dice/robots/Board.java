package com.linuxgods.dice.robots;

import java.awt.*;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.linuxgods.dice.robots.Board.Position.pos;

public class Board {
    public static final Dimension TILES = new Dimension(100, 100);
    private Map<Position, TileContent> tiles = new HashMap<>();
    private Random random = new Random();

    public Optional<TileContent> getTileContent(Position Position) {
        return Optional.ofNullable(tiles.get(Position));
    }

    public Position getPlayerCoordinate() {
        return getCoordinatesFor(TileContent.PLAYER)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No player!?"));
    }

    public Stream<Position> getCoordinatesFor(TileContent tileContent) {
        return tiles.entrySet().stream()
                .filter(entry -> entry.getValue() == tileContent)
                .map(Map.Entry::getKey);
    }

    public void setTileContent(Position Position, TileContent tileContent) {
        tiles.put(Position, tileContent);
    }

    public Position randomCoordinate() {
        return new Position(random.nextInt(TILES.width), random.nextInt(TILES.height));
    }

    public Stream<Position> positions() {
        return IntStream.range(0, TILES.height)
                .boxed()
                .flatMap(y -> IntStream.range(0, TILES.width)
                        .boxed()
                        .map(x -> pos(x, y)));
   }


    public enum TileContent {
        PLAYER,
        ALIEN,
        PILE;
    }

    public static class Position {
        public final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static Position pos(int x, int y) {
            return new Position(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position that = (Position) o;
            return Objects.equals(x, that.x) &&
                    Objects.equals(y, that.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

    }

}
