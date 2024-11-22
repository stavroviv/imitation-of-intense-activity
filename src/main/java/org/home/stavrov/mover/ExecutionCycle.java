package org.home.stavrov.mover;

public class ExecutionCycle {

    private static boolean isRunning;
    private static Thread executionThread;

    public static boolean process() {
        isRunning = !isRunning;
        if (isRunning) {
            startExecution();
        } else {
            stopExecution();
        }
        return isRunning;
    }

    private static void startExecution() {
        CommonMover[] movers = {
                new MouseMover(),
                new WindowSwitcher(),
                new ScreenshotChecker()
        };
        executionThread = new Thread(() -> {
            while (isRunning) {
                for (var mover : movers) {
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
