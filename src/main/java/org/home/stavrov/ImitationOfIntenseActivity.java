package org.home.stavrov;

import org.home.stavrov.mover.*;

import javax.swing.*;
import java.awt.*;

public class ImitationOfIntenseActivity {
    public static final int DELAY = 2000;
    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";

    private static boolean isRunning;
    private static Thread executionThread;

    public static void main(String[] args) {
        var frame = createMainFrame();
        addStartStopButton(frame);
        frame.setVisible(true);
    }

    private static void addStartStopButton(JFrame frame) {
        var moveButton = new JButton("Start");
        frame.add(moveButton, BorderLayout.CENTER);

        moveButton.addActionListener(e -> {
            if (isRunning) {
                isRunning = false;
                moveButton.setText("Start");
                stopExecution();
            } else {
                isRunning = true;
                moveButton.setText("Stop");
                CommonMover[] movers = {
//                new MouseMover(moveButton),
//                new WindowSwitcher(),
//                new MessageReplier(),
                        new ScreenshotChecker()
                };
                startExecution(movers);
            }
        });
    }

    private static JFrame createMainFrame() {
        var frame = new JFrame(MAIN_WINDOW_HEADER);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        return frame;
    }

    private static void startExecution(CommonMover... movers) {
        executionThread = new Thread(() -> {
            while (isRunning) {
                for (CommonMover mover : movers) {
                    if (isRunning) {
                        mover.run();
                    }
                }
            }
        });
        executionThread.start();
    }

    private static void stopExecution() {
        executionThread.interrupt();
    }

}
