package com.example.pepsell.UI;

import com.example.pepsell.Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ServerUI extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public ServerUI() {
        setTitle("Server Statistics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Message ID");
        tableModel.addColumn("Client ID");
        tableModel.addColumn("Message Text");
        tableModel.addColumn("Status");
        tableModel.addColumn("Delivered Time");

        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        setVisible(true);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable(Server.getMessageStat());
            }
        });
        timer.start();
    }

    public void updateTable(ArrayList<Statistic> statistics) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<Statistic> copyOfStatistics = new ArrayList<>(statistics); // Створюємо копію списку

            tableModel.setRowCount(0);

            for (Statistic stat : copyOfStatistics) {
                tableModel.addRow(new Object[]{
                        stat.getMessageId(),
                        stat.getClientId(),
                        stat.getMessage(),
                        stat.getStatus(),
                        stat.getDeliveredTime()
                });
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ServerUI();
        });
    }
}
