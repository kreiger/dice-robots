package com.linuxgods.dice.robots;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class BoardGraphicsComponent extends JComponent {

    private static final Dimension TILE_PIXELS = new Dimension(9,9);
    private static final Dimension BOARD_PADDING_PIXELS = new Dimension(5,5);
    private static final Dimension BOARD_WITH_PADDING_PIXELS = new Dimension(Board.TILES.width * TILE_PIXELS.width + BOARD_PADDING_PIXELS.width*2, Board.TILES.height * TILE_PIXELS.height + BOARD_PADDING_PIXELS.height*2);

    private static final Image PLAYER_IMAGE;

    static {
        try {
            PLAYER_IMAGE = ImageIO.read(BoardGraphicsComponent.class.getResourceAsStream("player.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Rectangle getTileRectangle(Board.Coordinate boardCoordinate) {
        Point tileCoordinate = new Point(boardCoordinate.x * TILE_PIXELS.width+ BOARD_PADDING_PIXELS.width, boardCoordinate.y * TILE_PIXELS.height+ BOARD_PADDING_PIXELS.height);
        return new Rectangle(tileCoordinate.x, tileCoordinate.y, TILE_PIXELS.width, TILE_PIXELS.height);
    }


    @Override
    public void paint(Graphics g) {
        for (int y = 0; y < Board.TILES.height; y++) {
            for (int x = 0; x < Board.TILES.width; x++) {
                Rectangle tile = getTileRectangle(new Board.Coordinate(x, y));
                g.drawRect(tile.x, tile.y, tile.width, tile.height);
                g.drawImage(PLAYER_IMAGE, tile.x, tile.y, null);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return BOARD_WITH_PADDING_PIXELS;
    }
}
