package org.home.stavrov.mover;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import static org.home.stavrov.ImitationOfIntenseActivity.MAIN_WINDOW_HEADER;

public class WindowSwitcher extends CommonMover {
    private final User32 user32 = User32.INSTANCE;
    private final List<WinDef.HWND> windowList = new ArrayList<>();
    private int currentIndex = 0;
    private final List<String> EXCLUDE_WIN = List.of(MAIN_WINDOW_HEADER, "Settings", "Program Manager");

    @Override
    protected void executeMoverStep() throws Exception {
        if (windowList.isEmpty()) {
            windowList.addAll(WindowUtils.getOpenWindows(EXCLUDE_WIN).keySet());
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
}
