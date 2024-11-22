package org.home.stavrov.mover;

import org.home.stavrov.utils.MouseUtils;
import org.home.stavrov.windows.MainWindow;

import java.awt.*;

public class MouseMover extends CommonMover {
    private final Robot robot;

    public MouseMover() {
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void executeMoverStep() {
        var moveButton = MainWindow.getExecutionButton();
        MouseUtils.moveMouseToBorderAndReturnAfterAction(() -> {
            for (var i = 0; i < 5; i++) {
                var buttonLocation = moveButton.getLocationOnScreen();
                var buttonSize = moveButton.getSize();
                var x = buttonLocation.x + (int) (Math.random() * buttonSize.width);
                var y = buttonLocation.y + (int) (Math.random() * buttonSize.height);
                robot.mouseMove(x, y);
                Thread.sleep(200);
            }
        });
    }
}
