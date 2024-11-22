package org.home.stavrov.windows;

import com.sun.jna.platform.win32.WinDef;
import org.home.stavrov.mover.ExecutionCycle;
import org.home.stavrov.utils.WindowInfo;
import org.home.stavrov.utils.WindowUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Map;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";
    private static JButton executionButton;

    public MainWindow() {
        setFrameProperties();
        addTable();
        addStartStopButton();
    }

    private void addStartStopButton() {
        executionButton = new JButton("Start");
        executionButton.addActionListener(e -> {
            boolean isRunning = ExecutionCycle.process();
            executionButton.setText(isRunning ? "Stop" : "Start");
        });
        add(executionButton, BorderLayout.SOUTH);
    }

    private void addTable() {
        JTable table = createWindowsTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static JButton getExecutionButton() {
        return executionButton;
    }

    private static JTable createWindowsTable() {
        String[] columnNames = {"№", "id", "Window Name"};
        Map<WinDef.HWND, WindowInfo> openWindows = WindowUtils.getOpenWindows();

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0){
            // Override isCellEditable to prevent editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };;
        int number = 1;
        for (Map.Entry<WinDef.HWND, WindowInfo> hwndWindowInfoEntry : openWindows.entrySet()) {
            var id = hwndWindowInfoEntry.getKey();
            var name = hwndWindowInfoEntry.getValue().getName();
            tableModel.addRow(new Object[]{number++, id, name});
        }
        JTable table = new JTable(tableModel);
//        table.onl
        TableColumn column = table.getColumnModel().getColumn(0);
//        column.edi
        column.setMaxWidth(30);

        column = table.getColumnModel().getColumn(1);
        column.setMaxWidth(100);
        column.setMinWidth(100);

        return table;
    }

    private void setFrameProperties() {
        setTitle(MAIN_WINDOW_HEADER);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
}
