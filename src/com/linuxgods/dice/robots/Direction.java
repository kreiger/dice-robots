package com.linuxgods.dice.robots;

import java.util.Optional;
import java.util.stream.Stream;

import static java.awt.event.KeyEvent.*;

public enum Direction {
    N(0, 1), NE(1, 1), E(1, 0), SE(1, -1), S(0, -1), SW(-1, -1), W(-1, 0), NW(-1, 1), TELEPORT(0, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    private static Direction forKeyCodeOrNull(int keyCode) {
        switch (keyCode) {
            case VK_UP :
            case VK_KP_UP :
            case VK_NUMPAD8:
            case VK_W:
                return N;
            case VK_NUMPAD9:
            case VK_E:
                return NE;
            case VK_RIGHT:
            case VK_KP_RIGHT:
            case VK_NUMPAD6:
            case VK_D:
                return E;
            case VK_NUMPAD3:
            case VK_C:
                return SE;
            case VK_DOWN:
            case VK_KP_DOWN:
            case VK_NUMPAD2:
            case VK_X:
                return S;
            case VK_NUMPAD1:
            case VK_Z:
                return SW;
            case VK_LEFT:
            case VK_KP_LEFT:
            case VK_NUMPAD4:
            case VK_A:
                return W;
            case VK_NUMPAD7:
            case VK_Q:
                return NW;
            case VK_T:
            case VK_SPACE:
                return TELEPORT;
        }
        return null;
    }

    public static Optional<Direction> forKeyCode(int keyCode) {
        return Optional.ofNullable(forKeyCodeOrNull(keyCode));
    }

    public static Direction forDelta(int deltaX, int deltaY) {
        int signX = Integer.signum(deltaX);
        int signY = Integer.signum(deltaY);
        return Stream.of(values())
                .filter(dir -> dir.dx == signX && dir.dy == signY)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No direction for delta " + deltaX + "," + deltaY));
    }
}
