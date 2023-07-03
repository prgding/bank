package com.cx.bank.test;

import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.manager.impl.ManagerImpl;
import com.cx.bank.model.MoneyBean;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * TestBank
 * 测试类
 *
 * @author dingshuai
 * @version 1.3
 */
public class TestBank {
    public static void main(String[] args) {
        MoneyBean moneyBean = new MoneyBean(new BigDecimal("100"));
        ManagerInterface manager = ManagerImpl.getInstance(moneyBean);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请输入操作编号：1.查询余额  2.取款  3.存款  4.退出系统");
            int operation = scanner.nextInt();

            switch (operation) {
                case 1 -> System.out.println("余额：" + manager.inquiry());
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
                case 4 -> manager.exitSystem();
                default -> System.out.println("无效的操作编号，请重新输入！");
            }
        }
    }
}
