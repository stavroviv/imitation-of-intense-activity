package org.home.stavrov.mover;

import static org.home.stavrov.ImitationOfIntenseActivity.DELAY;

public abstract class CommonMover {

    public void run() {
        try {
            executeMoverStep();
        } catch (Exception ex) {
            System.out.println("Exception " + this.getClass().getName());
        }
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            System.out.println("InterruptedException " + this.getClass().getName());
        }
    }

    protected abstract void executeMoverStep() throws Exception;
}
