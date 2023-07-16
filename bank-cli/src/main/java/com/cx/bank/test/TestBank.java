package com.cx.bank.test;


import com.cx.bank.manager.ManagerImpl;
import com.cx.bank.model.UserBean;
import java.util.Scanner;

public class TestBank {
    private static void printLoginMenu() {
        System.out.println("\n---------银行管理系统1.1---------");
        System.out.println("\t\t1.注册");
        System.out.println("\t\t2.登陆");
        System.out.println("\t\t3.退出系统");
        System.out.println("--------------------------------");
        System.out.print("请选择你的功能：");
    }

    private static void printFunctionMenu() {
        System.out.println("\n---------银行管理系统1.1---------");
        System.out.println("\t\t1.查询余额");
        System.out.println("\t\t2.取款");
        System.out.println("\t\t3.存款");
        System.out.println("\t\t4.转账");
        System.out.println("\t\t5.退出系统");
        System.out.println("--------------------------------");
        System.out.print("请选择你的功能：");
    }

    public static void main(String[] args) {

        ManagerImpl mi1 = new ManagerImpl();

        while (true) {
            printLoginMenu();
            int choice;
            Scanner s1 = new Scanner(System.in);
            choice = s1.nextInt();
            switch (choice) {
                case 1 -> mi1.register();
                case 2 -> mi1.login();
                case 3 -> System.exit(1);
            }
        }
    }

    public void functions(UserBean userBean) {
        ManagerImpl mi2 = new ManagerImpl(userBean);

        while (true) {
            printFunctionMenu();
            int choice;
            Scanner s1 = new Scanner(System.in);

            choice = s1.nextInt();
            switch (choice) {
                case 1 -> mi2.inquiry();
                case 2 -> {
                    mi2.withdraws();
                    mi2.inquiry();
                }
                case 3 -> {
                    mi2.deposit();
                    mi2.inquiry();
                }
                case 4 -> {
                    mi2.transfer();
                    mi2.inquiry();
                }
                case 5 -> mi2.exitSystem();
            }

        }
    }
}
