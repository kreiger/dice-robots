package com.linuxgods.dice.robots;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Board {
    public static final Dimension TILES = new Dimension(100, 100);
    private Map<Coordinate, TileContent> tiles = new HashMap<Coordinate, TileContent>();

    private Optional<TileContent> getTile(Coordinate coordinate) {
        return Optional.ofNullable(tiles.get(coordinate));
    }

    public Coordinate getPlayerCoordinate() {
        return getCoordinatesFor(TileContent.PLAYER)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No player!?"));
    }

    public Stream<Coordinate> getCoordinatesFor(TileContent tileContent) {
        return tiles.entrySet().stream()
                .filter(entry -> entry.getValue() == tileContent)
                .map(Map.Entry::getKey);
    }

    public void addPlayer(int x, int y) {
        tiles.put(new Coordinate(x,y), TileContent.PLAYER);
    }

    public enum TileContent {
        PLAYER,
        ROBOT,
        SCRAP;
    }

    public static class Coordinate {
        public final int x,y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return Objects.equals(x, that.x) &&
                    Objects.equals(y, that.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public boolean is(int x, int y) {
            return equals(new Coordinate(x,y));
        }
    }

}
