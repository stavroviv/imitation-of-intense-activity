package org.home.stavrov.windows;

import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.mover.CommonMover;
import org.home.stavrov.mover.MouseMover;
import org.home.stavrov.mover.ScreenshotChecker;
import org.home.stavrov.mover.WindowSwitcher;
import org.home.stavrov.utils.WindowInfo;
import org.home.stavrov.utils.WindowUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";

    private static boolean isRunning;
    private static Thread executionThread;

    public MainWindow() {
        createMainFrame();
        addStartStopButton();
//        setVisible(true);
    }

    private  void addStartStopButton() {
        var moveButton = new JButton("Start");
//        frame.add(moveButton, BorderLayout.CENTER);

        moveButton.addActionListener(e -> {
            if (isRunning) {
                isRunning = false;
                moveButton.setText("Start");
                stopExecution();
            } else {
                isRunning = true;
                moveButton.setText("Stop");
                CommonMover[] movers = {
                        new MouseMover(moveButton),
                        new WindowSwitcher(),
                        new ScreenshotChecker()
                };
                startExecution(movers);
            }
        });


        JTable table = createWindowsTable();
        JScrollPane scrollPane = new JScrollPane(table);
//        scrollPane.setPreferredSize();
//        scrollPane.add(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create a button for adding rows
//        JButton addButton = new JButton("Add Row");
        add(moveButton, BorderLayout.SOUTH);
    }

    private static JTable createWindowsTable() {
        String[] columnNames = {"№", "id", "Window Name"};
        Map<WinDef.HWND, WindowInfo> openWindows = WindowUtils.getOpenWindows();

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int number = 1;
        for (Map.Entry<WinDef.HWND, WindowInfo> hwndWindowInfoEntry : openWindows.entrySet()) {
            var id = hwndWindowInfoEntry.getKey();
            var name = hwndWindowInfoEntry.getValue().getName();
            tableModel.addRow(new Object[]{number++, id, name});
        }
        // 0 initial rows
        JTable table = new JTable(tableModel);
        TableColumn column = table.getColumnModel().getColumn(0); // ID column
        column.setMaxWidth(30); // Set width for ID column

        column = table.getColumnModel().getColumn(1); // Name column
        column.setMaxWidth(100); // Set width for Name column
        column.setMinWidth(100);
        column = table.getColumnModel().getColumn(2); // Age column
//        column.setPreferredWidth(50); // Set width for Age column
        return table;
    }

    private void createMainFrame() {
//        var frame = new JFrame(MAIN_WINDOW_HEADER);
        setTitle(MAIN_WINDOW_HEADER);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        return frame;
    }

    private static void startExecution(CommonMover... movers) {
        executionThread = new Thread(() -> {
            while (isRunning) {
                for (CommonMover mover : movers) {
                    if (isRunning) {
                        mover.run();
                    }
                }
            }
        });
        executionThread.start();
    }

    private static void stopExecution() {
        executionThread.interrupt();
    }

}
