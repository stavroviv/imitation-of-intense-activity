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

            setPreferredSize(new Dimension(600, 450));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            var frameWidth = getWidth();
            var frameHeight = getHeight();
            g.drawImage(image1, 0, 15, frameWidth / 2, frameHeight, this);
            g.drawImage(image2, frameWidth / 2, 15, frameWidth / 2, frameHeight, this);
        }
    }
}
