package org.home.stavrov.mover;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.utils.WindowInfo;
import org.home.stavrov.utils.WindowUtils;

import java.awt.*;
import java.util.Map;

public class MessageReplier extends CommonMover {
    private static final User32 user32 = User32.INSTANCE;

    private final Robot robot;

    public MessageReplier() {
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void executeMoverStep() {
        // TODO
        Map<WinDef.HWND, WindowInfo> teams =
                WindowUtils.getOpenWindowsByFilter(name -> name.contains("Microsoft Teams"));
        System.out.println(teams);
        //            List<WinDef.HWND> openWindows = WindowUtils.getOpenWindows();
//        Map<WinDef.HWND, WindowInfo> openWindows = WindowUtils.getOpenWindows();
//        for (Map.Entry<WinDef.HWND, WindowInfo> hwndWindowInfoEntry : openWindows.entrySet()) {
//            String winName = hwndWindowInfoEntry.getValue().getName();
//            if (winName.contains("Chrome")) {
//                user32.ShowWindow(hwndWindowInfoEntry.getKey(), User32.SW_RESTORE);
//                user32.SetForegroundWindow(hwndWindowInfoEntry.getKey());
//                robot.keyPress(KeyEvent.VK_F5);
//            }
//        }
//        System.out.println(openWindows);
    }

}
