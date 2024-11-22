package org.home.stavrov.mover;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.utils.*;
import org.home.stavrov.windows.ImageDisplayWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class ScreenshotChecker extends CommonMover {

    private static final String HITS_PATTERN = "\\b\\d{1,3}(,\\d{3})* hits\\b";

    private final User32 user32 = User32.INSTANCE;
    private static String prev;
    private static String curr;
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
        curr = ImageUtils.parseImage(currImage, HITS_PATTERN);

        if (prev != null && !curr.equals(prev)) {
            imageDisplayWindow.addImages(prevImage, currImage, 0);
            imageDisplayWindow.setVisible(true);
            SoundUtils.playSound();
        }
        prev = curr;
        prevImage = currImage;
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
}
