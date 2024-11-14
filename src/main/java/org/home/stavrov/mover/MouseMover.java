package org.home.stavrov.mover;

import javax.swing.*;
import java.awt.*;

import static org.home.stavrov.ImitationOfIntenseActivity.DELAY;

public class MouseMover implements Runnable {
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
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                var buttonLocation = moveButton.getLocationOnScreen();
                var buttonSize = moveButton.getSize();
                var x = buttonLocation.x + (int) (Math.random() * buttonSize.width);
                var y = buttonLocation.y + (int) (Math.random() * buttonSize.height);
                robot.mouseMove(x, y);
                Thread.sleep(DELAY);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException MouseMover");
                return;
            }
        }
        System.out.println("stoped MouseMover");
    }
}
