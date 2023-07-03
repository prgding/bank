package com.cx.bank.test;

import com.cx.bank.manager.ManagerImpl;
import com.cx.bank.model.MoneyBean;

import java.math.BigDecimal;
import java.util.Scanner;

public class TestBank {
    public static void main(String[] args) {
        MoneyBean moneyBean = new MoneyBean(new BigDecimal("100"));
        ManagerImpl manager = new ManagerImpl(moneyBean);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请输入操作编号：1.查询余额  2.取款  3.存款  4.退出系统");
            int operation = scanner.nextInt();

            switch (operation) {
                case 1 -> System.out.println("余额：" + manager.inquiry());
                case 2 -> {
                    System.out.println("请输入取款金额:");
                    BigDecimal withdrawal = scanner.nextBigDecimal();
                    manager.withdrawals(withdrawal);
                }
                case 3 -> {
                    System.out.println("请输入存款金额:");
                    BigDecimal deposit = scanner.nextBigDecimal();
                    manager.deposit(deposit);
                }
                case 4 -> manager.exitSystem();
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }
}
