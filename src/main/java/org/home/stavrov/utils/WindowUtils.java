package org.home.stavrov.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class WindowUtils {

    private static final User32 user32 = User32.INSTANCE;

    private WindowUtils() {
    }

    public static Map<String, WindowInfo> getOpenWindows() {
        return getOpenWindows(Collections.emptyList());
    }

    public static Map<String, WindowInfo> getOpenWindows(List<String> excludeWin) {
        return getOpenWindowsByFilter(windowName -> !excludeWin.contains(windowName));
    }

    public static Map<String, WindowInfo> getOpenWindowsByFilter(Predicate<String> filter) {
        Map<String, WindowInfo> openedWindows = new HashMap<>();
        user32.EnumWindows((hwnd, pointer) -> {
            if (user32.IsWindowVisible(hwnd) && user32.GetWindowTextLength(hwnd) > 0) {
                var windowText = new char[512];
                user32.GetWindowText(hwnd, windowText, 512);
                var windowName = Native.toString(windowText).trim();
                if (filter.test(windowName)) {
                    var value = new WindowInfo();
                    value.setName(windowName);
                    value.setId(hwnd);
                    openedWindows.put(hwnd.toString(), value);
                }
//                System.out.println("Window title: " + windowName);
            }
            return true;
        }, Pointer.NULL);
        return openedWindows;
    }

    public static void showBalloonNotification(String message) {
        var balloon = new JWindow();
        balloon.setLayout(new BorderLayout());

        var messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageLabel.setBackground(new Color(155, 186, 236)); // Light yellow background
        messageLabel.setOpaque(true);

        balloon.add(messageLabel, BorderLayout.CENTER);

        balloon.setSize(350, 80);

        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        var x = screenSize.width - balloon.getWidth() - 10; // 10 pixels from right edge
        var y = screenSize.height - balloon.getHeight() - 50; // 50 pixels from bottom edge
        balloon.setLocation(x, y);
        balloon.setAlwaysOnTop(true);
        balloon.setVisible(true);

        var timer = new Timer(5000, e -> {
            balloon.setVisible(false);
            balloon.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
