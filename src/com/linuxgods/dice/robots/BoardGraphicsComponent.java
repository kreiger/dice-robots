package com.linuxgods.dice.robots;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

class BoardGraphicsComponent extends JComponent implements Game.Listener {

    private static final Dimension CAMERA = new Dimension(27, 27);
    private static final Dimension TILE = new Dimension(27, 27);
    private static final Dimension PADDING = new Dimension(0, 0);
    private static final Dimension MINI_MAP_SIZE = new Dimension(8, 8);
    private static final Dimension VISIBLE = new Dimension(CAMERA.width * TILE.width, CAMERA.height * TILE.height);
    private static final Dimension BOARD_WITH_PADDING = new Dimension(VISIBLE.width + PADDING.width * 2, VISIBLE.height + PADDING.height * 2);

    private static final Color GRID_LINE_COLOR = new Color(183, 183, 183);
    private static final Color GRID_FILL_COLOR = new Color(231, 231, 231);

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
        int x = boardPosition.x * TILE.width + PADDING.width;
        int y = VISIBLE.height - TILE.height - boardPosition.y * TILE.height + PADDING.height;
        Point tileCoordinate = new Point(x, y);
        return new Rectangle(tileCoordinate.x, tileCoordinate.y, TILE.width, TILE.height);
    }

    private Image drawMiniMap(Graphics g) {

        final BufferedImage minimap = new BufferedImage(Board.TILES.width, Board.TILES.height, BufferedImage.TYPE_INT_ARGB);
        board.positions().forEach(setMiniMapColor(minimap));
        g.drawImage(minimap, PADDING.width, BOARD_WITH_PADDING.height - TILE.height*MINI_MAP_SIZE.height - PADDING.height, TILE.width*MINI_MAP_SIZE.width, TILE.height*MINI_MAP_SIZE.height, null);

        return minimap;
    }

    private Consumer<Board.Position> setMiniMapColor(BufferedImage minimap) {
        return pos -> {
            int color = getMiniMapColor(pos);
            minimap.setRGB(pos.x, minimap.getHeight()-pos.y-1, color);
        };
    }

    private int getMiniMapColor(Board.Position pos) {
        return board.getTileContent(pos)
                        .map(this::getMiniMapColor)
                        .orElse(new Color(184, 181, 184).getRGB());
    }


    @Override
    public void paint(Graphics g) {

        Board.Position from = board.getPlayerPosition().plus(-CAMERA.width / 2, -CAMERA.height / 2);
        Board.Position to = board.getPlayerPosition().plus(CAMERA.width / 2 + 1, CAMERA.height / 2 + 1);

        board.positions(from, to).forEach(pos -> {
            Rectangle tile = getTileRectangle(pos.plus(-board.getPlayerPosition().x + CAMERA.width / 2, -board.getPlayerPosition().y + CAMERA.height / 2));
            g.setColor(GRID_FILL_COLOR);
            g.fillRect(tile.x, tile.y, tile.width, tile.height);
            g.setColor(GRID_LINE_COLOR);
            g.drawRect(tile.x, tile.y, tile.width, tile.height);
            board.getTileContent(pos)
                    .map(this::getImage)
                    .ifPresent(image -> g.drawImage(image, tile.x, tile.y, tile.width, tile.height, null));
        });

        drawMiniMap(g);

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

    private int getMiniMapColor(Board.TileContent tileContent) {
        switch (tileContent) {
            case PLAYER:
                return new Color(0, 151, 8).getRGB();
            case PILE:
                return new Color(255, 252, 28).getRGB();
            case ALIEN:
                return new Color(255, 0, 0).getRGB();
            default:
                return new Color(184, 181, 184).getRGB();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return BOARD_WITH_PADDING;
    }

    @Override
    public void notifyBoardUpdated(Board board) {
        this.board = board;
        repaint();
    }
}
