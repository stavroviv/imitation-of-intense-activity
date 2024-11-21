package org.home.stavrov.mover;

import static org.home.stavrov.ImitationOfIntenseActivity.DELAY;

public abstract class CommonMover {

    public void run() {
        try {
            executeMoverStep();
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            System.out.println("Stopped execution " + this.getClass().getName());
        } catch (Exception ex) {
            System.out.println("InterruptedException " + this.getClass().getName());
        }
    }

    protected abstract void executeMoverStep() throws Exception;
}
