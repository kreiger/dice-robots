package com.linuxgods.dice.robots;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class BoardGraphicsComponent extends JComponent implements Game.Listener {

    private static final Dimension CAMERA = new Dimension(27,27);
    private static final Dimension TILE = new Dimension(27,27);
    private static final Dimension PADDING = new Dimension(0,0);
    private static final Dimension VISIBLE = new Dimension(CAMERA.width * TILE.width, CAMERA.height * TILE.height);
    private static final Dimension BOARD_WITH_PADDING = new Dimension(VISIBLE.width+ PADDING.width*2, VISIBLE.height+ PADDING.height*2);

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
        int x = boardPosition.x * TILE.width + PADDING.width;
        int y = VISIBLE.height - TILE.height - boardPosition.y * TILE.height + PADDING.height;
        Point tileCoordinate = new Point(x,y);
        return new Rectangle(tileCoordinate.x, tileCoordinate.y, TILE.width, TILE.height);
    }


    @Override
    public void paint(Graphics g) {

        Board.Position from = board.getPlayerPosition().plus(-CAMERA.width/2, -CAMERA.height/2);
        Board.Position to = board.getPlayerPosition().plus(CAMERA.width/2+1, CAMERA.height/2+1);

        board.positions(from, to).forEach(pos -> {
            Rectangle tile = getTileRectangle(pos.plus(-board.getPlayerPosition().x+CAMERA.width/2, -board.getPlayerPosition().y+CAMERA.height/2));
            g.setColor(GRID_FILL_COLOR);
            g.fillRect(tile.x, tile.y, tile.width, tile.height);
            g.setColor(GRID_LINE_COLOR);
            g.drawRect(tile.x, tile.y, tile.width, tile.height);
            board.getTileContent(pos)
                    .map(this::getImage)
                    .ifPresent(image -> g.drawImage(image, tile.x, tile.y, tile.width, tile.height, null));
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
        return BOARD_WITH_PADDING;
    }

    @Override
    public void notifyBoardUpdated(Board board) {
        this.board = board;
        repaint();
    }
}
