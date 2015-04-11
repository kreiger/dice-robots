package com.linuxgods.dice.robots;

import java.awt.*;

public class Board {
    public static final Dimension TILES = new Dimension(100, 100);

    public static class Coordinate {
        public final int x,y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
