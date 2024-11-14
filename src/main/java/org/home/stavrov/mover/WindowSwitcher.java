package org.home.stavrov.mover;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.util.ArrayList;
import java.util.List;

import static org.home.stavrov.ImitationOfIntenseActivity.DELAY;
import static org.home.stavrov.ImitationOfIntenseActivity.MAIN_WINDOW_HEADER;

public class WindowSwitcher implements Runnable {
    private final User32 user32 = User32.INSTANCE;
    private final List<WinDef.HWND> windowList = new ArrayList<>();
    private int currentIndex = 0;
    private final List<String> EXCLUDE_WIN = List.of(MAIN_WINDOW_HEADER, "Settings", "Program Manager");

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                switchToNextWindow();
                Thread.sleep(DELAY);
            } catch (Exception ex) {
                System.out.println("InterruptedException WindowSwitcher");
                return;
            }
        }
        System.out.println("stoped WindowSwitcher");
    }

    public void switchToNextWindow() throws Exception {
        if (windowList.isEmpty()) {
            updateWindowList();
        }
        if (windowList.isEmpty()) {
            System.out.println("No windows found.");
            return;
        }
        currentIndex = (currentIndex + 1) % windowList.size();
        var nextWindow = windowList.get(currentIndex);
        user32.ShowWindow(nextWindow, User32.SW_RESTORE);
        var result = user32.SetForegroundWindow(nextWindow);
        System.out.println(result);
        var i = 0;
        while (!result && i < 5) {
            Thread.sleep(1000);
            result = user32.SetForegroundWindow(nextWindow);
//            user32.BringWindowToTop(nextWindow);
//            user32.SetFocus(nextWindow);
            System.out.println(result);
            i++;
        }

    }

    private void updateWindowList() {
        windowList.clear();
        user32.EnumWindows((hwnd, pointer) -> {
            if (user32.IsWindowVisible(hwnd) && user32.GetWindowTextLength(hwnd) > 0) {
                var windowText = new char[512];
                user32.GetWindowText(hwnd, windowText, 512);
                var wText = Native.toString(windowText).trim();
                if (!EXCLUDE_WIN.contains(wText)) {
                    windowList.add(hwnd);
                }
                System.out.println("Window title: " + wText);
            }
            return true;
        }, Pointer.NULL);
    }
}
