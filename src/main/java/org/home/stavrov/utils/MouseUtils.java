package org.home.stavrov.utils;

import java.awt.*;
import java.util.function.Supplier;

public class MouseUtils {

    private MouseUtils() {

    }

    public static void moveMouseToBorderAndReturnAfterAction(VoidAction action) {
        moveMouseToBorderAndReturnAfterAction(() -> {
            try {
                action.doAction();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public static <T> T moveMouseToBorderAndReturnAfterAction(Supplier<T> action) {
        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        Point location = MouseInfo.getPointerInfo().getLocation();
        int xMousePrev = location.x;
        int yMousePrev = location.y;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        robot.mouseMove(screenWidth, screenHeight);

        T result = action.get();

        robot.mouseMove(xMousePrev, yMousePrev);

        return result;
    }
}
