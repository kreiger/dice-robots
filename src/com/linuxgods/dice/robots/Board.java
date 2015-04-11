package com.linuxgods.dice.robots;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.linuxgods.dice.robots.Board.Position.pos;
import static com.linuxgods.dice.robots.Board.TileContent.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Board {
    public static final Dimension TILES = new Dimension(100, 100);
    private Map<Position, TileContent> tiles = new HashMap<>();
    private Random random = new Random();

    public Optional<TileContent> getTileContent(Position Position) {
        return Optional.ofNullable(tiles.get(Position));
    }

    public Optional<Position> getPlayerPosition() {
        return getPositionsFor(tc -> tc == PLAYER || tc == TOMBSTONE)
                .findFirst();
    }

    public Stream<Position> getPositionsFor(TileContent tileContent) {
        return getPositionsFor(tc -> tc == tileContent);
    }

    public Stream<Position> getPositionsFor(Predicate<TileContent> tileContentPredicate) {
        return tiles.entrySet().stream()
                .filter(entry -> tileContentPredicate.test(entry.getValue()))
                .map(Map.Entry::getKey);
    }

    public void setTileContent(Position Position, TileContent tileContent) {
        tiles.put(Position, tileContent);
    }

    public Position randomEmptyPosition() {
        Position position = getRandomPosition();
        while (getTileContent(position).isPresent()) {
            position = getRandomPosition();
        }
        return position;
    }

    private Position getRandomPosition() {
        return new Position(random.nextInt(TILES.width), random.nextInt(TILES.height));
    }

    public Stream<Position> positions() {
        return positions(pos(0, 0), pos(TILES.width, TILES.height));
    }

    public Stream<Position> positions(Position from, Position to) {

        return IntStream.range(max(0, from.y), min(to.y, TILES.width))
                .boxed()
                .flatMap(y -> IntStream.range(max(0, from.x), min(to.x, TILES.height))
                        .boxed()
                        .map(x -> pos(x, y)));
   }

    public Position movePlayer(Direction direction) {
        if (direction.equals(Direction.TELEPORT)) {
            return randomEmptyPosition();
        }

        final Position initialPlayerPosition = getPlayerPosition().orElseThrow(() -> new RuntimeException("No player!"));
        final Position newPosition = initialPlayerPosition.move(direction);
        if (isOutOfBounds(newPosition)) {
            return initialPlayerPosition;
        }
        return newPosition;
    }

    private boolean isOutOfBounds(Position position) {
        return position.x < 0 || position.y < 0 || position.x >= TILES.width || position.y >= TILES.height;
    }

    public Game.Phase getPhase() {
        if (!getPositionsFor(tc -> tc == PLAYER).findFirst().isPresent()) {
            return Game.Phase.LOST;
        }
        if (0 == getPositionsFor(ALIEN).count()) {
            return Game.Phase.WON;
        }
        return Game.Phase.PLAYING;
    }


    public enum TileContent {

        PLAYER(false, "player.png", new Color(0, 151, 8)),
        ALIEN(true, "alien.png", new Color(151, 0, 6)),
        PILE(true, "pile.png", new Color(255, 252, 28)),
        TOMBSTONE(false, "tomb.png",  new Color(53, 53, 53));

        private final boolean deadly;
        private final Color color;
            private final Image image;

        TileContent(boolean deadly, String fileName, Color color) {
            this.deadly = deadly;
            this.color = color;

            try {
                this.image = ImageIO.read(Board.class.getResourceAsStream(fileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean isDeadly() {
            return deadly;
        }

        public Image getImage() {
            return image;
        }

        public Color getColor() {
            return color;

        }
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

        public Position move(Direction direction) {
            return new Position(x + direction.getDx(), y + direction.getDy());
        }

        public Position plus(int x, int y) {
            return new Position(this.x + x, this.y + y);
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


        public Direction getDirectionTo(Position position) {
            return Direction.forDelta(position.x - x, position.y - y);
        }
    }

}
