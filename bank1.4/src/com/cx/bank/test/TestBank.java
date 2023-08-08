package com.cx.bank.test;

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

/**
 * TestBank
 * 测试类
 *
 * @author dingshuai
 * @version 1.4
 */
public class TestBank {
    public static void main(String[] args) {
        ManagerInterface unLoggedIn = ManagerImpl.getInstance();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== 请输入操作编号：1.注册  2.登录  3.退出系统 ===");
            switch (scanner.nextInt()) {
                case 1 -> {
                    try {
                        unLoggedIn.register();
                    } catch (FileNotFoundException e) {
                        System.out.println("请先在项目根目录下创建 userInfo 文件夹\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 2 -> {
                    try {
                        ManagerInterface loggedIn = unLoggedIn.login();
                        if (loggedIn != null) {
                            manageFunction(loggedIn);
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("没有此用户，请先注册\n");
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 3 -> {
                    try {
                        unLoggedIn.exitSystem();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }

    public static void manageFunction(ManagerInterface manager) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("=== 请输入操作编号：1.查询余额  2.取款  3.存款  4.退出系统 ===");
            switch (scanner.nextInt()) {
                case 1 -> System.out.println("余额：" + manager.inquiry() + "\n");
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
                    try {
                        manager.exitSystem();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }
}
