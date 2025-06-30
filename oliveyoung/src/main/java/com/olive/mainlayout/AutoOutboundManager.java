package com.olive.mainlayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.olive.common.model.Stock;
import com.olive.common.model.User;
import com.olive.common.repository.StockDAO;
import com.olive.common.view.Page;

public class AutoOutboundManager {

    private MainLayout mainLayout;
    private User user;
    private JLabel lb_alertCount;

    private List<String> autoOutboundLog = Collections.synchronizedList(new ArrayList<>());
    private int alertCount = 0;

    private Thread autoOutboundThread;
    private volatile boolean running = true;

    public AutoOutboundManager(MainLayout mainLayout, User user, JLabel lb_alertCount) {
        this.mainLayout = mainLayout;
        this.user = user;
        this.lb_alertCount = lb_alertCount;
    }

    public void start() {
        autoOutboundThread = new Thread(() -> {
            StockDAO stockDAO = new StockDAO();

            while (running) {
                try {
                    List<Stock> stockList = stockDAO.selectAllStockWithQuantity(user);

                    for (Stock stock : stockList) {
                        if (stock.getSt_quantity() > 0) {
                            if (!running) break;

                            int newQty = stock.getSt_quantity() - 1;
                            stock.setSt_quantity(newQty);
                            stockDAO.updateQuantity(stock.getSt_id(), newQty, user);

                            String logEntry = "- 재고 ID: " + stock.getSt_id() + ", 남은 수량: " + newQty;
                            autoOutboundLog.add(logEntry);
                            System.out.println("자동 출고 - " + logEntry);

                            alertCount++;
                            SwingUtilities.invokeLater(() -> lb_alertCount.setText(String.valueOf(alertCount)));

                            // 패널 업데이트
                            mainLayout.setDataDirty(true);
                            mainLayout.refreshIfDirty();
                        }
                        Thread.sleep(60 * 1000);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        autoOutboundThread.setDaemon(true);
        autoOutboundThread.start();
    }

    public void stop() {
        running = false;
    }

    public void resetAlertCount() {
        alertCount = 0;
        lb_alertCount.setText("0");
    }

    public void showAutoOutboundLogDialog() {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("자동 출고 기록");
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            for (String log : autoOutboundLog) {
                textArea.append(log + "\n");
            }

            JScrollPane scrollPane = new JScrollPane(textArea);
            panel.add(scrollPane, BorderLayout.CENTER);

            JButton btnClose = new JButton("닫기");
            btnClose.addActionListener(ev -> {
            	dialog.dispose();
            	autoOutboundLog.clear();
            });
            panel.add(btnClose, BorderLayout.SOUTH);

            dialog.add(panel);
            dialog.setVisible(true);
        });
    }
}
