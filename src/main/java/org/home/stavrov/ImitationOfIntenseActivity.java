package org.home.stavrov;

import org.home.stavrov.mover.CommonMover;
import org.home.stavrov.mover.MessageReplier;
import org.home.stavrov.mover.MouseMover;
import org.home.stavrov.mover.WindowSwitcher;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ImitationOfIntenseActivity {
    public static final int DELAY = 3000;
    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";

    private static final List<Thread> threads = new ArrayList<>();
    private static boolean isRunning;

    public static void main(String[] args) {
        var frame = createMainFrame();
        addStartStopButton(frame);
        frame.setVisible(true);
    }

    private static void addStartStopButton(JFrame frame) {
        var moveButton = new JButton("Start/Stop");
        frame.add(moveButton, BorderLayout.CENTER);
        CommonMover[] movers = {
                new MouseMover(moveButton),
                new WindowSwitcher(),
                new MessageReplier()
        };
        moveButton.addActionListener(e -> {
            if (isRunning) {
                isRunning = false;
                stopThreads();
            } else {
                isRunning = true;
                startThreads(movers);
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

    private static void startThreads(CommonMover... movers) {
        for (Runnable mover : movers) {
            Thread thread = new Thread(mover);
            thread.start();
            threads.add(thread);
        }
    }

    private static void stopThreads() {
        for (Thread thread : threads) {
            if (thread != null && thread.isAlive()) {
                try {
                    thread.interrupt();
                    thread.join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        threads.clear();
    }

}
