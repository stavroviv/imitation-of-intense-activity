package org.home.stavrov.windows;

import org.home.stavrov.mover.ExecutionContext;
import org.home.stavrov.mover.ExecutionCycle;
import org.home.stavrov.utils.WindowUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainWindow extends JFrame {
    public static final String MAIN_WINDOW_HEADER = "Imitation Of Intense Activity";
    private static JButton executionButton;
    private JButton refreshButton;
    private JTextField windowFollowPattern;
    private DefaultTableModel openWindowsTableModel;
    private JTable openWindowsTable;

    public MainWindow() {
        setFrameProperties();
        addTable();
        addStartStopButton();
    }

    private void addStartStopButton() {
        executionButton = new JButton("Start");
        executionButton.addActionListener(event -> processExecution());
        executionButton.setPreferredSize(new Dimension(100, 300));
        add(executionButton, BorderLayout.SOUTH);
    }

    private void processExecution() {
        var selectedRow = openWindowsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select window to follow");
            return;
        }
        var id = (String) openWindowsTableModel.getValueAt(selectedRow, 1);
        ExecutionContext.setWindowToFollowId(id);
        ExecutionContext.setPatternToFollow(windowFollowPattern.getText());
        var isRunning = ExecutionCycle.process();
        executionButton.setText(isRunning ? "Stop" : "Start");
        disableWindowTable(isRunning);
    }

    private void disableWindowTable(boolean isRunning) {
        openWindowsTable.setEnabled(!isRunning);
        refreshButton.setEnabled(!isRunning);
        windowFollowPattern.setEnabled(!isRunning);
    }

    private void addTable() {
        var table = createWindowsTable();

        var panel = new JPanel();
        panel.setLayout(new BorderLayout());

        var panelFlow = new JPanel();
        panelFlow.setLayout(new FlowLayout(FlowLayout.LEFT));

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refreshWindowTable());
        panelFlow.add(refreshButton);

        windowFollowPattern = new JTextField(50);
        windowFollowPattern.setText("\\b\\d{1,3}(,\\d{3})* hits\\b");
        panelFlow.add(windowFollowPattern);

        panel.add(panelFlow, BorderLayout.NORTH);

        var scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    private void refreshWindowTable() {
        openWindowsTableModel.setRowCount(0);
        var openWindows = WindowUtils.getOpenWindows();
        var number = 1;
        for (var hwndWindowInfoEntry : openWindows.entrySet()) {
            var id = hwndWindowInfoEntry.getKey();
            var name = hwndWindowInfoEntry.getValue().getName();
            openWindowsTableModel.addRow(new Object[]{number++, id, name});
        }
        openWindowsTable.setRowSelectionInterval(0, 0);
    }

    public static JButton getExecutionButton() {
        return executionButton;
    }

    private JTable createWindowsTable() {
        var columnNames = new String[]{"№", "id", "Window Name"};
        openWindowsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        openWindowsTable = new JTable(openWindowsTableModel);
        refreshWindowTable();
        setColumnWidth();
        openWindowsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return openWindowsTable;
    }

    private void setColumnWidth() {
        var columnModel = openWindowsTable.getColumnModel();
        var column = columnModel.getColumn(0);
        column.setMaxWidth(30);
        column = columnModel.getColumn(1);
        column.setMaxWidth(100);
        column.setMinWidth(100);
    }

    private void setFrameProperties() {
        setTitle(MAIN_WINDOW_HEADER);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
}
