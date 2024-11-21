package org.home.stavrov.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageDisplayWindow extends JFrame {

    public ImageDisplayWindow() {
        // Set the title of the window
        setTitle("Screenshot difference");
        // Set the size of the window
        setSize(800, 600);
        // Make the window close when the user closes it
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a custom panel to display the images

    }

    public void addImages(BufferedImage image1, BufferedImage image2) {
        add(new ImagePanel(image1, image2));
    }

    static class ImagePanel extends JPanel {
        private BufferedImage image1;
        private BufferedImage image2;

        public ImagePanel(BufferedImage image1, BufferedImage image2) {
            this.image1 = image1;
            this.image2 = image2;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Get the current size of the JFrame (window)
            int frameWidth = getWidth();
            int frameHeight = getHeight();

            // Calculate half of the width and height for the images
            int width1 = frameWidth / 2;  // Half of the JFrame's width
            int height1 = frameHeight * 80 / 100; // Half of the JFrame's height

            int width2 = frameWidth / 2;  // Half of the JFrame's width
            int height2 = frameHeight * 80 / 100; // Half of the JFrame's height

            // Draw the first BufferedImage at the specified position (x, y) with resizing
            g.drawImage(image1, 0, 0, width1, height1, this);

            // Draw the second BufferedImage at a different position with resizing
            g.drawImage(image2, frameWidth / 2 , 0, width2, height2, this);
        }
    }
}
