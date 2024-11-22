package org.home.stavrov.windows;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class ImageDisplayWindow extends JFrame {
    private final JPanel mainPanel;
    private final JScrollPane scrollPane;

    public ImageDisplayWindow() {
        setTitle("Screenshot difference");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
    }

    public void addImages(BufferedImage image1, BufferedImage image2, String difference) {
        mainPanel.add(new ImagePanel(image1, image2, difference));
        SwingUtilities.invokeLater(() -> {
            mainPanel.revalidate();
            mainPanel.repaint();
            var verticalScrollBar = scrollPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });

    }

    static class ImagePanel extends JPanel {
        private final BufferedImage image1;
        private final BufferedImage image2;

        public ImagePanel(BufferedImage image1, BufferedImage image2, String difference) {
            this.image1 = image1;
            this.image2 = image2;
            setLayout(new BorderLayout());

            // Create and add the JTextArea at the top
            var textArea = new JTextArea("Time: " + LocalDateTime.now() + " " + difference);
            textArea.setEditable(false);
            add(textArea, BorderLayout.NORTH);

            setPreferredSize(new Dimension(600, 650));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Get the current size of the JFrame (window)
            var frameWidth = getWidth();
            var frameHeight = getHeight();

            // Calculate half of the width and height for the images
            var width1 = frameWidth / 2;  // Half of the JFrame's width
            var height1 = frameHeight * 80 / 100; // Half of the JFrame's height

            var width2 = frameWidth / 2;  // Half of the JFrame's width
            var height2 = frameHeight * 80 / 100; // Half of the JFrame's height

            // Draw the first BufferedImage at the specified position (x, y) with resizing
            g.drawImage(image1, 0, 15, width1, height1, this);

            // Draw the second BufferedImage at a different position with resizing
            g.drawImage(image2, frameWidth / 2, 15, width2, height2, this);
        }
    }
}
