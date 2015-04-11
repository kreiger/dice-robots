package com.linuxgods.dice.robots;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

class ViewComponent extends JComponent implements Game.State.Listener {

    private static final Dimension CAMERA = new Dimension(27, 27);
    private static final Dimension TILE = new Dimension(27, 27);
    private static final Dimension PADDING = new Dimension(0, 0);
    private static final Dimension VISIBLE = new Dimension(CAMERA.width * TILE.width, CAMERA.height * TILE.height);
    private static final Dimension BOARD_WITH_PADDING = new Dimension(VISIBLE.width + PADDING.width * 2, VISIBLE.height + PADDING.height * 2);

    private static final Color GRID_LINE_COLOR = new Color(183, 183, 183);
    private static final Color GRID_FILL_COLOR = new Color(217, 217, 217);

    private static final Image PLAYER_IMAGE;
    private static final Image ALIEN_IMAGE;
    private static final Image PILE_IMAGE;

    static {
        try {
            PLAYER_IMAGE = ImageIO.read(ViewComponent.class.getResourceAsStream("player.png"));
            ALIEN_IMAGE = ImageIO.read(ViewComponent.class.getResourceAsStream("alien.png"));
            PILE_IMAGE = ImageIO.read(ViewComponent.class.getResourceAsStream("pile.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Game.State state = new Game.State(Optional.<Board>empty(), Game.Phase.START);

    ViewComponent() {
    }

    private static Rectangle getTileRectangle(Board.Position boardPosition) {
        int x = boardPosition.x * TILE.width + PADDING.width;
        int y = VISIBLE.height - TILE.height - boardPosition.y * TILE.height + PADDING.height;
        Point tileCoordinate = new Point(x, y);
        return new Rectangle(tileCoordinate.x, tileCoordinate.y, TILE.width, TILE.height);
    }

    @Override
    public void paint(Graphics g) {
        drawBoard(g);
        g.setColor(Color.GREEN);
        g.drawString("Alien robots from Mars " + state.phase, 100, 100);
    }

    private void drawBoard(Graphics g) {
        state.getBoard()
                .ifPresent(board -> board.getPlayerPosition().ifPresent(playerPosition -> {
                    Board.Position from = playerPosition.plus(-CAMERA.width / 2, -CAMERA.height / 2);
                    Board.Position to = playerPosition.plus(CAMERA.width / 2 + 1, CAMERA.height / 2 + 1);

                    board.positions(from, to).forEach(pos -> {
                        Rectangle tile = getTileRectangle(pos.plus(-playerPosition.x + CAMERA.width / 2, -playerPosition.y + CAMERA.height / 2));
                        g.setColor(GRID_FILL_COLOR);
                        g.fillRect(tile.x, tile.y, tile.width, tile.height);
                        g.setColor(GRID_LINE_COLOR);
                        g.drawRect(tile.x, tile.y, tile.width, tile.height);
                        board.getTileContent(pos)
                                .map(this::getImage)
                                .ifPresent(image -> g.drawImage(image, tile.x, tile.y, tile.width, tile.height, null));
                    });
                }));
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
    public void notify(Game.State state) {
        this.state = state;
        repaint();
    }
}
