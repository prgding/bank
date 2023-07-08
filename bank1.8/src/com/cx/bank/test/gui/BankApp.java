package com.cx.bank.test.gui;

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;
import com.cx.bank.util.MD5Utils;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * BankApp
 * GUI 界面
 *
 * @author dingshuai
 * @version 1.6
 */
public class BankApp {
    private ManagerInterface manager;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField amountField;
    private JTextField recipientField;


    public BankApp() {
        manager = ManagerImpl.getInstance();

        frame = new JFrame("欢迎界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(4, 2));

        frame.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        frame.add(usernameField);

        frame.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        frame.add(passwordField);

        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = MD5Utils.hash(new String(passwordField.getPassword()));
            try {
                String msg = manager.register(username, password);
                JOptionPane.showMessageDialog(frame, msg);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });
        frame.add(registerButton);

        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = MD5Utils.hash(new String(passwordField.getPassword()));
            try {
                ManagerInterface loggedIn = manager.login(username, password);
                if (loggedIn != null) {
                    JOptionPane.showMessageDialog(frame, "登录成功！");
                    showBankingWindow();
                }
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "没有此用户，请先注册");
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
        frame.add(loginButton);

        JButton exitButton = new JButton("退出系统");
        exitButton.addActionListener(e -> {
            try {
                manager.exitSystem();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        frame.add(exitButton);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new BankApp();
    }

    private void showBankingWindow() {
        frame = new JFrame("业务界面");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(5, 1));

        JButton checkBalanceButton = new JButton("查询余额");
        checkBalanceButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "余额: " + manager.getMoneyBean().getBalance() + " 元"));
        frame.add(checkBalanceButton);

        JButton withdrawButton = new JButton("取款");
        withdrawButton.addActionListener(e -> showDepositWindow());
        frame.add(withdrawButton);

        JButton depositButton = new JButton("存款");
        depositButton.addActionListener(e -> showWithdrawWindow());
        frame.add(depositButton);

        JButton transferButton = new JButton("转账");
        transferButton.addActionListener(e -> showTransferWindow());
        frame.add(transferButton);

        JButton exitButton = new JButton("退出系统");
        exitButton.addActionListener(e -> {
            try {
                manager.exitSystem();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        frame.add(exitButton);
        frame.setVisible(true);
    }


    private void showWithdrawWindow() {
        frame = new JFrame("存款");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(2, 2));

        frame.add(new JLabel("存款:"));
        amountField = new JTextField();
        frame.add(amountField);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e -> {
            String amountText = amountField.getText();
            try {
                BigDecimal amount = new BigDecimal(amountText);
                manager.deposit(amount);
                JOptionPane.showMessageDialog(frame, "存款成功！");
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> frame.dispose());
        frame.add(cancelButton);
        frame.add(confirmButton);

        frame.setVisible(true);
    }

    private void showDepositWindow() {
        frame = new JFrame("取款");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(2, 2));

        frame.add(new JLabel("取款金额:"));
        amountField = new JTextField();
        frame.add(amountField);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e -> {
            String amountText = amountField.getText();
            try {
                BigDecimal amount = new BigDecimal(amountText);
                manager.withdrawals(amount);
                JOptionPane.showMessageDialog(frame, "取款成功！");
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> frame.dispose());
        frame.add(cancelButton);
        frame.add(confirmButton);

        frame.setVisible(true);
    }

    private void showTransferWindow() {
        frame = new JFrame("转账界面");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        frame.add(new JLabel("收款方账户:"));
        recipientField = new JTextField();
        frame.add(recipientField);

        frame.add(new JLabel("转账金额:"));
        amountField = new JTextField();
        frame.add(amountField);

        JButton confirmButton = new JButton("确定");
        confirmButton.addActionListener(e -> {
            String recipient = recipientField.getText();
            String amountText = amountField.getText();
            try {
                BigDecimal amount = new BigDecimal(amountText);
                manager.transfer(recipient, amount);
                JOptionPane.showMessageDialog(frame, "转账成功！");
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> frame.dispose());
        frame.add(cancelButton);
        frame.add(confirmButton);

        frame.setVisible(true);
    }
}
