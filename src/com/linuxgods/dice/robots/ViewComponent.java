package com.linuxgods.dice.robots;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

class ViewComponent extends JComponent implements Game.State.Listener {

    private static final Dimension CAMERA = new Dimension(27, 27);
    private static final Dimension TILE = new Dimension(27, 27);
    private static final Dimension PADDING = new Dimension(0, 0);
    private static final Dimension MINI_MAP_SIZE = new Dimension(8, 8);

    private static final Dimension VISIBLE = new Dimension(CAMERA.width * TILE.width, CAMERA.height * TILE.height);
    private static final Dimension BOARD_WITH_PADDING = new Dimension(VISIBLE.width + PADDING.width * 2, VISIBLE.height + PADDING.height * 2);

    private static final Color GRID_LINE_COLOR = new Color(183, 183, 183);
    private static final Color GRID_FILL_COLOR = new Color(231, 231, 231);

    private Game.State state;

    ViewComponent(Game.State state) {
        this.state = state;
    }

    private static Rectangle getTileRectangle(Board.Position boardPosition) {
        int x = boardPosition.x * TILE.width + PADDING.width;
        int y = VISIBLE.height - TILE.height - boardPosition.y * TILE.height + PADDING.height;
        Point tileCoordinate = new Point(x, y);
        return new Rectangle(tileCoordinate.x, tileCoordinate.y, TILE.width, TILE.height);
    }

    private Image drawMiniMap(Graphics g, Board board) {

        final BufferedImage minimap = new BufferedImage(Board.TILES.width, Board.TILES.height, BufferedImage.TYPE_INT_ARGB);
        board.positions().forEach(setMiniMapColor(minimap, board));
        g.drawImage(minimap, PADDING.width, BOARD_WITH_PADDING.height - TILE.height * MINI_MAP_SIZE.height - PADDING.height, TILE.width * MINI_MAP_SIZE.width, TILE.height * MINI_MAP_SIZE.height, null);

        return minimap;
    }

    private Consumer<Board.Position> setMiniMapColor(BufferedImage minimap, Board board) {
        return pos -> {
            int color = getMiniMapColor(pos, board).getRGB();
            minimap.setRGB(pos.x, minimap.getHeight() - pos.y - 1, color);
        };
    }

    private Color getMiniMapColor(Board.Position pos, Board board) {
        return board.getTileContent(pos)
                .map(Board.TileContent::getColor)
                .orElse(new Color(184, 181, 184, 130));
    }

    @Override
    public void paint(Graphics g) {
        drawBoard(g);
        if (state.getPhase() != Game.Phase.PLAYING) {
            drawTitel(g);
        }
    }

    private void drawTitel(Graphics g) {
        final String string = getStringToPrint();

        g.setColor(Color.RED);
        g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 24));
        final Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(string, g);
        g.drawString(string, getPreferredSize().width / 2 - (int) stringBounds.getWidth() / 2, 100);
    }

    private String getStringToPrint() {
        switch (state.getPhase()) {
            case START:
                return "Alien robots from Mars";
            case LOST:
                return "You lose!";
            case WON:
                return "A winner is you, now play next level!";
            default:
                return "What?";
        }
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
                                .map(Board.TileContent::getImage)
                                .ifPresent(image -> g.drawImage(image, tile.x, tile.y, tile.width, tile.height, null));
                    });
                    drawMiniMap(g, board);
                }));
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
