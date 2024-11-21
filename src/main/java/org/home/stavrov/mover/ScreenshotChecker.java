package org.home.stavrov.mover;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.utils.MouseUtils;
import org.home.stavrov.utils.SoundUtils;
import org.home.stavrov.utils.WindowInfo;
import org.home.stavrov.utils.WindowUtils;
import org.home.stavrov.windows.ImageDisplayWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Map;

public class ScreenshotChecker extends CommonMover {

    private final User32 user32 = User32.INSTANCE;
    private static int[] prev;
    private static int[] curr;
    private final Robot robot;
    private static BufferedImage prevImage;
    private static BufferedImage currImage;

    ImageDisplayWindow imageDisplayWindow;

    public ScreenshotChecker() {
        super();
        this.imageDisplayWindow = new ImageDisplayWindow();
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void executeMoverStep() throws Exception {
        Map<WinDef.HWND, WindowInfo> teams =
                WindowUtils.getOpenWindowsByFilter(name -> name.contains("Prod D7MVS logs"));
        if (teams.isEmpty()) {
            System.out.println("Logs window not found");
            return;
        }
        currImage = captureWindow(teams.keySet().iterator().next());
        curr = calculateHistogram(currImage);

        if (prev != null) {
            double similarity = calculateSimilarity(curr, prev);
            System.out.println(similarity);
            if (similarity < 0.94 && similarity > 0.5) {
                imageDisplayWindow.addImages(prevImage, currImage, similarity);
                imageDisplayWindow.setVisible(true);
                SoundUtils.playSound();
            }
        }
        prev = curr;
        prevImage = currImage;
    }

    private static double calculateSimilarity(int[] hist1, int[] hist2) {
        double sum = 0;
        double sum1 = Arrays.stream(hist1).sum();
        double sum2 = Arrays.stream(hist2).sum();
        for (int i = 0; i < hist1.length; i++) {
            double norm1 = hist1[i] / sum1;
            double norm2 = hist2[i] / sum2;
            sum += Math.min(norm1, norm2); // Intersection of histograms
        }
        return sum; // Value between 0 (no similarity) and 1 (identical)
    }

    private BufferedImage captureWindow(WinDef.HWND hwnd) throws Exception {
        user32.ShowWindow(hwnd, User32.SW_RESTORE);
        boolean b = user32.SetForegroundWindow(hwnd);
        int i = 0;
        while (!b && i < 5) {
            Thread.sleep(1000);
            b = user32.SetForegroundWindow(hwnd);
            i++;
        }

        User32 user32 = User32.INSTANCE;
        WinDef.RECT rect = new WinDef.RECT();
        user32.GetWindowRect(hwnd, rect);

        int dpiScalingFactor = getDPIScalingFactor();
        int x = rect.left * 96 / dpiScalingFactor;
        int y = rect.top * 96 / dpiScalingFactor;
        int width = (rect.right - rect.left) * 96 / dpiScalingFactor;
        int height = (rect.bottom - rect.top) * 96 / dpiScalingFactor;

        // Create a Rectangle representing the client area to capture
        Rectangle captureArea = new Rectangle(x, y, width, height);

        return MouseUtils
                .moveMouseToBorderAndReturnAfterAction(() -> robot.createScreenCapture(captureArea));
    }

    private static int getDPIScalingFactor() {
        WinDef.HDC hdc = User32.INSTANCE.GetDC(null);  // Get the device context for the screen
        int dpiX = GDI32.INSTANCE.GetDeviceCaps(hdc, 88);  // Horizontal DPI
        User32.INSTANCE.ReleaseDC(null, hdc); // Release the device context
        return dpiX;
    }

    private static int[] calculateHistogram(BufferedImage image) {
        int[] histogram = new int[256]; // Grayscale histogram
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                int gray = ((rgb >> 16 & 0xff) + (rgb >> 8 & 0xff) + (rgb & 0xff)) / 3; // Average RGB
                histogram[gray]++;
            }
        }
        return histogram;
    }
}
