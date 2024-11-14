package org.home.stavrov;

import org.home.stavrov.mover.MouseMover;
import org.home.stavrov.mover.WindowSwitcher;

import javax.swing.*;
import java.awt.*;

public class ImitationOfIntenseActivity {
    public static final int DELAY = 10000;
    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";

    private static Thread moveThread;
    private static Thread windowsSwitchThread;
    private static boolean isRunning;

    public static void main(String[] args) {
        var frame = createMainFrame();
        addStartStopButton(frame);
        frame.setVisible(true);
    }

    private static void addStartStopButton(JFrame frame) {
        var moveButton = new JButton("Start/Stop");
        frame.add(moveButton, BorderLayout.CENTER);
        moveButton.addActionListener(e -> {
            if (isRunning) {
                isRunning = false;
                stopThreads();
            } else {
                isRunning = true;
                startThreads(moveButton);
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

    private static void startThreads(JButton moveButton) {
        moveThread = new Thread(new MouseMover(moveButton));
        moveThread.start();
        windowsSwitchThread = new Thread(new WindowSwitcher());
        windowsSwitchThread.start();
    }

    private static void stopThreads() {
        stopThread(moveThread);
        stopThread(windowsSwitchThread);
    }

    private static void stopThread(Thread thread) {
        if (thread != null && thread.isAlive()) {
            try {
                thread.interrupt();
                thread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
