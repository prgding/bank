package com.cx.bank.test.gui;

/**
 * BankApp
 *
 * @author dingshuai
 * @version 1.6
 */

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class BankApp {
    private ManagerInterface manager;
    private JFrame frame;
    private JTextField amountField;
    private JLabel balanceLabel;

    public BankApp() {
        manager = ManagerImpl.getInstance();

        frame = new JFrame("Bank App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        amountField = new JTextField();
        frame.add(amountField);

        JButton withdrawButton = new JButton("取款");
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BigDecimal amount = new BigDecimal(amountField.getText());
                try {
                    manager.withdrawals(amount);
                    updateBalance();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
        frame.add(withdrawButton);

        balanceLabel = new JLabel();
        frame.add(balanceLabel);

        updateBalance();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new BankApp();
    }

    private void updateBalance() {
        balanceLabel.setText("余额: " + manager.getMoneyBean().getBalance());
    }
}
