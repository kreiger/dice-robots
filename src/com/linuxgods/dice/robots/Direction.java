package com.linuxgods.dice.robots;

import java.util.Optional;

import static java.awt.event.KeyEvent.*;

public enum Direction {
    N, NE, E, SE, S, SW, W, NW;

    private static Direction forKeyCodeOrNull(int keyCode) {
        switch (keyCode) {
            case VK_UP :
            case VK_KP_UP :
            case VK_NUMPAD8:
                return N;
            case VK_NUMPAD9:
                return NE;
            case VK_RIGHT:
            case VK_KP_RIGHT:
            case VK_NUMPAD6:
                return E;
            case VK_NUMPAD3:
                return SE;
            case VK_DOWN:
            case VK_KP_DOWN:
            case VK_NUMPAD2:
                return S;
            case VK_NUMPAD1:
                return SW;
            case VK_LEFT:
            case VK_KP_LEFT:
            case VK_NUMPAD4:
                return W;
            case VK_NUMPAD7:
                return NW;

        }
        return null;
    }

    public static Optional<Direction> forKeyCode(int keyCode) {
        return Optional.ofNullable(forKeyCodeOrNull(keyCode));
    }
}
