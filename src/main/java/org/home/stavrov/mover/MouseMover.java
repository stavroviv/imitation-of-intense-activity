package org.home.stavrov.mover;

import org.home.stavrov.utils.MouseUtils;

import javax.swing.*;
import java.awt.*;

public class MouseMover extends CommonMover {
    private final JButton moveButton;
    private final Robot robot;

    public MouseMover(JButton moveButton) {
        this.moveButton = moveButton;
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void executeMoverStep() {
        MouseUtils.moveMouseToBorderAndReturnAfterAction(() -> {
            for (int i = 0; i < 5; i++) {
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
