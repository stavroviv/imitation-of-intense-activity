package org.home.stavrov.mover;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.utils.WindowInfo;
import org.home.stavrov.utils.WindowUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

public class ScreenshotChecker extends CommonMover {

    private final User32 user32 = User32.INSTANCE;
    int i = 0;
    private static byte[] prev;
    private static byte[] curr;

    @Override
    protected void executeMoverStep() throws Exception {
        Map<WinDef.HWND, WindowInfo> teams =
                WindowUtils.getOpenWindowsByFilter(name -> name.contains("Prod D7MVS logs"));
        BufferedImage bufferedImage = captureWindow(teams.keySet().iterator().next());
        curr = calculateImageHash(bufferedImage);
        if (prev != null && !Arrays.equals(prev,curr)) {
            System.out.println("Change!!! prev: " + new String(prev) + " curr: " + new String(curr));
        }
        prev = curr;
//        System.out.println("Hash: " + new String(bytes));
    }

    private BufferedImage captureWindow(WinDef.HWND hwnd) throws Exception {
        user32.ShowWindow(hwnd, User32.SW_RESTORE);
        user32.SetForegroundWindow(hwnd);
        Thread.sleep(1000);
        User32 user32 = User32.INSTANCE;
        WinDef.RECT rect = new WinDef.RECT();
        user32.GetWindowRect(hwnd, rect);

        int dpiScalingFactor = getDPIScalingFactor(hwnd);
        int x = rect.left * 96 / dpiScalingFactor;
        int y = rect.top * 96 / dpiScalingFactor;
        int width = (rect.right - rect.left) * 96 / dpiScalingFactor;
        int height = (rect.bottom - rect.top) * 96 / dpiScalingFactor;

        // Create a Rectangle representing the client area to capture
        Rectangle captureArea = new Rectangle(x, y, width, height);

        // Create a Robot object
        Robot robot = new Robot();

        // Capture the region as a BufferedImage
        BufferedImage screenCapture = robot.createScreenCapture(captureArea);
//        File outputFile = new File("screenshot" + i++ + ".png");
//        ImageIO.write(screenCapture, "PNG", outputFile);
        return screenCapture;
    }

    private static int getDPIScalingFactor(WinDef.HWND hwnd) {
        WinDef.HDC hdc = User32.INSTANCE.GetDC(null);  // Get the device context for the screen
        int dpiX = GDI32.INSTANCE.GetDeviceCaps(hdc, 88);  // Horizontal DPI
        User32.INSTANCE.ReleaseDC(null, hdc); // Release the device context

        // Returning the DPI scaling factor
        return dpiX;
    }

    private static byte[] calculateImageHash(BufferedImage image) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixel = image.getRGB(x, y);
                digest.update((byte) (pixel & 0xFF)); // Blue
                digest.update((byte) ((pixel >> 8) & 0xFF)); // Green
                digest.update((byte) ((pixel >> 16) & 0xFF)); // Red
            }
        }
        return digest.digest();
    }
}
