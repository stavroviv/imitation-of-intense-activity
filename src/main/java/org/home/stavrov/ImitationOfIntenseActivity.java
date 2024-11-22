package org.home.stavrov;

import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.mover.CommonMover;
import org.home.stavrov.mover.MouseMover;
import org.home.stavrov.mover.ScreenshotChecker;
import org.home.stavrov.mover.WindowSwitcher;
import org.home.stavrov.utils.WindowInfo;
import org.home.stavrov.utils.WindowUtils;
import org.home.stavrov.windows.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class ImitationOfIntenseActivity {
    public static final int DELAY = 15000;
//    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";
//
//    private static boolean isRunning;
//    private static Thread executionThread;

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setVisible(true);
//        var frame = createMainFrame();
//        addStartStopButton(frame);
//        frame.setVisible(true);
    }

}
