package com.bookstore.util;

import java.awt.Color;

public final class ThemeManager {
    public enum Mode { LIGHT, DARK }

    private static Mode currentMode = Mode.LIGHT;

    private ThemeManager() {}

    public static void toggleMode() {
        currentMode = (currentMode == Mode.LIGHT) ? Mode.DARK : Mode.LIGHT;
    }

    public static Mode getMode() {
        return currentMode;
    }

    public static Color primary() {
        return currentMode == Mode.LIGHT ? new Color(0x8B4513) : new Color(0x1A1A2E);
    }

    public static Color text() {
        return currentMode == Mode.LIGHT ? Color.BLACK : Color.WHITE;
    }

    public static Color background() {
        return currentMode == Mode.LIGHT ? new Color(245, 234, 221) : new Color(15, 18, 46);
    }
}
