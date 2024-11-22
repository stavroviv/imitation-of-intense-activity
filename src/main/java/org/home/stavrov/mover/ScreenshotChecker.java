package org.home.stavrov.mover;

import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.utils.ImageParsingUtils;
import org.home.stavrov.utils.MouseUtils;
import org.home.stavrov.utils.SoundUtils;
import org.home.stavrov.utils.WindowUtils;
import org.home.stavrov.windows.ImageDisplayWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ScreenshotChecker extends CommonMover {

    private final User32 user32 = User32.INSTANCE;
    private static String prev;
    private static String curr;
    private final Robot robot;
    private static BufferedImage prevImage;
    private static BufferedImage currImage;

    private ImageDisplayWindow imageDisplayWindow;

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
        var openWindows = WindowUtils.getOpenWindows();
        var windowInfo = openWindows.get(ExecutionContext.getWindowToFollowId());
        if (Objects.isNull(windowInfo)) {
            System.out.println("window not found");
            return;
        }

        currImage = captureWindow(windowInfo.getId());
        curr = ImageParsingUtils.parseImage(currImage, ExecutionContext.getPatternToFollow());

        if (prev != null && !curr.equals(prev) && !prev.isEmpty() && !curr.isEmpty()) {
            imageDisplayWindow.addImages(prevImage, currImage, "Prev value: " + prev + " curr value: " + curr);
            imageDisplayWindow.setVisible(true);
            SoundUtils.playSound();
        }
        prev = curr;
        prevImage = currImage;
    }

    private BufferedImage captureWindow(WinDef.HWND hwnd) throws Exception {
        user32.ShowWindow(hwnd, User32.SW_RESTORE);
        var b = user32.SetForegroundWindow(hwnd);
        var i = 0;
        while (!b && i < 5) {
            Thread.sleep(1000);
            b = user32.SetForegroundWindow(hwnd);
            i++;
        }

        var user32 = User32.INSTANCE;
        var rect = new WinDef.RECT();
        user32.GetWindowRect(hwnd, rect);

        var dpiScalingFactor = getDPIScalingFactor();
        var x = rect.left * 96 / dpiScalingFactor;
        var y = rect.top * 96 / dpiScalingFactor;
        var width = (rect.right - rect.left) * 96 / dpiScalingFactor;
        var height = (rect.bottom - rect.top) * 96 / dpiScalingFactor;

        // Create a Rectangle representing the client area to capture
        var captureArea = new Rectangle(x, y, width, height);

        return MouseUtils
                .moveMouseToBorderAndReturnAfterAction(() -> robot.createScreenCapture(captureArea));
    }

    private static int getDPIScalingFactor() {
        var hdc = User32.INSTANCE.GetDC(null);  // Get the device context for the screen
        var dpiX = GDI32.INSTANCE.GetDeviceCaps(hdc, 88);  // Horizontal DPI
        User32.INSTANCE.ReleaseDC(null, hdc); // Release the device context
        return dpiX;
    }
}
