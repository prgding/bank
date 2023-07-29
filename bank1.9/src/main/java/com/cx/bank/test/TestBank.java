package com.cx.bank.test;

import com.cx.bank.exception.AccountOverDrawnException;
import com.cx.bank.exception.InvalidDepositException;
import com.cx.bank.manager.ManagerImpl;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.model.Account;
import com.cx.bank.model.Log;
import com.cx.bank.util.MD5Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

/**
 * TestBank
 * 测试类
 *
 * @author dingshuai
 * @version 1.9
 */
public class TestBank {
    public static void main(String[] args) {
        ManagerInterface unLoggedIn = ManagerImpl.getInstance();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== 请输入操作编号：1.注册  2.登录  3.退出系统 ===");
            switch (scanner.nextInt()) {
                case 1 -> {
                    System.out.println("请输入用户名:");
                    String username = scanner.next();
                    System.out.println("请输入密码:");
                    String password = MD5Utils.hash(scanner.next());
                    String msg = unLoggedIn.register(username, password);
                    System.out.println(msg);
                }
                case 2 -> {
                    try {
                        System.out.println("请输入用户名:");
                        String username = scanner.next();
                        System.out.println("请输入密码:");
                        String password = MD5Utils.hash(scanner.next());
                        ManagerInterface loggedIn = unLoggedIn.login(username, password);
                        if (loggedIn != null) {
                            if (loggedIn.getUserBean().getUsername().equals("admin")) {
                                adminFunction(loggedIn);
                            } else {
                                manageFunction(loggedIn);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("没有此用户，请先注册\n");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> unLoggedIn.exitSystem();
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }

    private static void adminFunction(ManagerInterface loggedIn) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== 请输入操作编号：1.查询所有账户状态  2.冻结账户  3.解冻账户  4.退出系统===");
            switch (scanner.nextInt()) {
                case 1 -> showAllAccountFlags(loggedIn);
                case 2 -> {
                    System.out.println("请输入要冻结的用户名:");
                    String username = scanner.next();
                    loggedIn.freezeUser(username);
                    showNewFlag(loggedIn, username);
                }
                case 3 -> {
                    System.out.println("请输入要解冻的用户名:");
                    String username = scanner.next();
                    loggedIn.unfreezeUser(username);
                    showNewFlag(loggedIn, username);
                }
                case 4 -> loggedIn.exitSystem();
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }

    private static void showAllAccountFlags(ManagerInterface loggedIn) {
        List<Account> accounts = loggedIn.inquiryAllUser();
        System.out.println("用户名, 用户状态");
        for (Account act : accounts) {
            System.out.println(act.getUsername() + ", " + (act.getUserFlag() == 1 ? "正常" : "冻结"));
        }
    }

    private static void showNewFlag(ManagerInterface loggedIn, String username) {
        Account act = loggedIn.inquiryUser(username);
        System.out.println("用户名, 用户状态");
        System.out.println(username + ", " + (act.getUserFlag() == 1 ? "正常" : "冻结"));
    }

    public static void manageFunction(ManagerInterface manager) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== 请输入操作编号：1.查询余额  2.取款  3.存款  4.转账  5.查询操作明细  6.退出系统 ===");
            switch (scanner.nextInt()) {
                case 1 -> System.out.println("余额：" + manager.inquiry(manager.getUserBean().getUsername()) + "\n");
                case 2 -> {
                    System.out.println("请输入取款金额:");
                    BigDecimal withdrawal = scanner.nextBigDecimal();
                    try {
                        manager.withdrawals(withdrawal);
                    } catch (AccountOverDrawnException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.println("请输入存款金额:");
                    BigDecimal deposit = scanner.nextBigDecimal();
                    try {
                        manager.deposit(deposit);
                    } catch (InvalidDepositException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 4 -> {
                    System.out.println("输入对方账户名:");
                    String toName = scanner.next();
                    System.out.println("输入转账金额:");
                    BigDecimal transMoney = scanner.nextBigDecimal();
                    try {
                        manager.transfer(toName, transMoney);
                    } catch (AccountOverDrawnException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> {
                    String username = manager.getUserBean().getUsername();
                    List<Log> logs = manager.findLogsByName(username);
                    System.out.println("操作编号, 操作类型, 操作金额, 用户编号");
                    for (Log log : logs) {
                        System.out.printf("%s, %s, %s, %s%n", log.getLogId(), log.getLogType(), log.getLogAmount(), log.getUserId());

                    }
                }
                case 6 -> manager.exitSystem();
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }
}
