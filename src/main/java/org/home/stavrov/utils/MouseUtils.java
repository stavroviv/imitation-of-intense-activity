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
        var location = MouseInfo.getPointerInfo().getLocation();
        var xMousePrev = location.x;
        var yMousePrev = location.y;
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        var screenWidth = (int) screenSize.getWidth();
        var screenHeight = (int) screenSize.getHeight();

        robot.mouseMove(screenWidth, screenHeight);

        var result = action.get();

        robot.mouseMove(xMousePrev, yMousePrev);

        return result;
    }
}
