package org.home.stavrov.utils;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class WindowUtils {

    private static final User32 user32 = User32.INSTANCE;

    private WindowUtils() {
    }

    public static Map<WinDef.HWND, WindowInfo> getOpenWindows() {
        return getOpenWindows(Collections.emptyList());
    }

    public static Map<WinDef.HWND, WindowInfo> getOpenWindows(List<String> excludeWin) {
        return getOpenWindowsByFilter(windowName -> !excludeWin.contains(windowName));
    }

    public static Map<WinDef.HWND, WindowInfo> getOpenWindowsByFilter(Predicate<String> filter) {
        Map<WinDef.HWND, WindowInfo> openedWindows = new HashMap<>();
        user32.EnumWindows((hwnd, pointer) -> {
            if (user32.IsWindowVisible(hwnd) && user32.GetWindowTextLength(hwnd) > 0) {
                var windowText = new char[512];
                user32.GetWindowText(hwnd, windowText, 512);
                var windowName = Native.toString(windowText).trim();
                if (filter.test(windowName)) {
                    WindowInfo value = new WindowInfo();
                    value.setName(windowName);
                    openedWindows.put(hwnd, value);
                }
                System.out.println("Window title: " + windowName);
            }
            return true;
        }, Pointer.NULL);
        return openedWindows;
    }

}
