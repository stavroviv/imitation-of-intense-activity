package org.home.stavrov.mover;

import static org.home.stavrov.ImitationOfIntenseActivity.DELAY;

public abstract class CommonMover {

    public void run() {
        try {
            executeMoverStep();
            Thread.sleep(DELAY);
        } catch (Exception ex) {
            System.out.println("InterruptedException " + this.getClass().getName());
            return;
        }
        System.out.println("stoped " + this.getClass().getName());
    }

    protected abstract void executeMoverStep() throws Exception;
}
