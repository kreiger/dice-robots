package com.linuxgods.dice.robots;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class BoardGraphicsComponent extends JComponent implements Game.Listener {

    private static final Dimension TILE_PIXELS = new Dimension(9,9);
    private static final Dimension BOARD_PADDING_PIXELS = new Dimension(5,5);
    private static final Dimension BOARD_PIXELS = new Dimension(Board.TILES.width * TILE_PIXELS.width, Board.TILES.height * TILE_PIXELS.height);
    private static final Dimension BOARD_WITH_PADDING_PIXELS = new Dimension(BOARD_PIXELS.width+BOARD_PADDING_PIXELS.width*2, BOARD_PIXELS.height+BOARD_PADDING_PIXELS.height*2);
    private static final Color GRID_LINE_COLOR = new Color(183, 183, 183);
    private static final Color GRID_FILL_COLOR = new Color(217, 217, 217);

    private static final Image PLAYER_IMAGE;
    private static final Image ALIEN_IMAGE;
    private static final Image PILE_IMAGE;

    static {
        try {
            PLAYER_IMAGE = ImageIO.read(BoardGraphicsComponent.class.getResourceAsStream("player.png"));
            ALIEN_IMAGE = ImageIO.read(BoardGraphicsComponent.class.getResourceAsStream("alien.png"));
            PILE_IMAGE = ImageIO.read(BoardGraphicsComponent.class.getResourceAsStream("pile.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Board board;

    BoardGraphicsComponent(Board board) {
        this.board = board;
    }

    private static Rectangle getTileRectangle(Board.Position boardPosition) {
        Point tileCoordinate = new Point(boardPosition.x * TILE_PIXELS.width+ BOARD_PADDING_PIXELS.width, BOARD_PIXELS.height - TILE_PIXELS.height - boardPosition.y * TILE_PIXELS.height + BOARD_PADDING_PIXELS.height);
        return new Rectangle(tileCoordinate.x, tileCoordinate.y, TILE_PIXELS.width, TILE_PIXELS.height);
    }


    @Override
    public void paint(Graphics g) {
        board.positions().forEach(pos -> {
            Rectangle tile = getTileRectangle(pos);
            g.setColor(GRID_FILL_COLOR);
            g.fillRect(tile.x, tile.y, tile.width, tile.height);
            g.setColor(GRID_LINE_COLOR);
            g.drawRect(tile.x, tile.y, tile.width, tile.height);
            board.getTileContent(pos)
                    .map(this::getImage)
                    .ifPresent(image -> g.drawImage(image, tile.x, tile.y, null));
        });
    }

    private Image getImage(Board.TileContent tileContent) {
        switch (tileContent) {
            case PLAYER:
                return PLAYER_IMAGE;
            case PILE:
                return PILE_IMAGE;
            default:
                return ALIEN_IMAGE;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return BOARD_WITH_PADDING_PIXELS;
    }

    @Override
    public void notifyBoardUpdated(Board board) {
        this.board = board;
        repaint();
    }
}
