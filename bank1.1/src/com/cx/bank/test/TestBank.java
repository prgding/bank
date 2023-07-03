package com.cx.bank.test;

import com.cx.bank.manager.ManagerImpl;
import com.cx.bank.model.MoneyBean;

import java.math.BigDecimal;
import java.util.Scanner;

public class TestBank {
    public static void main(String[] args) {
        MoneyBean moneyBean = new MoneyBean(new BigDecimal("100"));
        ManagerImpl manager = ManagerImpl.getInstance(moneyBean);
        ManagerImpl manager2 = ManagerImpl.getInstance(moneyBean);

        if (manager == manager2) {
            System.out.println("manager 和 manager2 是同一个实例，实现了单例");
        } else {
            System.out.println("manager 和 manager2 不是同一个实例，未实现单例");
        }

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
