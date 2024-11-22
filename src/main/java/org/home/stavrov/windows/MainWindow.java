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

    private  void processExecution() {
        int selectedRow = openWindowsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select window to follow");
            return;
        }
        var id = (String) openWindowsTableModel.getValueAt(selectedRow, 1);

        boolean isRunning = ExecutionCycle.process();
        executionButton.setText(isRunning ? "Stop" : "Start");
    }

    private void addTable() {
        JTable table = createWindowsTable();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel panelFlow = new JPanel();
        panelFlow.setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton button = new JButton("Refresh");
        button.addActionListener(e -> refreshWindowTable());
        panelFlow.add(button);

        panel.add(panelFlow, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }

    private void refreshWindowTable() {
        openWindowsTableModel.setRowCount(0);
        var openWindows = WindowUtils.getOpenWindows();
        int number = 1;
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
        String[] columnNames = {"№", "id", "Window Name"};
        openWindowsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        openWindowsTable = new JTable(openWindowsTableModel);
        refreshWindowTable();
        setColumnWidth(openWindowsTable);

        return openWindowsTable;
    }

    private static void setColumnWidth(JTable table) {
        TableColumn column = table.getColumnModel().getColumn(0);
        column.setMaxWidth(30);
        column = table.getColumnModel().getColumn(1);
        column.setMaxWidth(100);
        column.setMinWidth(100);
    }

    private void setFrameProperties() {
        setTitle(MAIN_WINDOW_HEADER);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
}
