package com.cx.bank.test;

import com.cx.bank.manager.ManagerImpl;
import com.cx.bank.manager.ManagerInterface;
import com.cx.bank.util.AccountOverDrawnException;
import com.cx.bank.util.InvalidDepositException;

import java.io.IOException;
import java.util.Scanner;

/**
 * 测试类
 *
 * @author Administrator
 */
public class TestBank {
    static String logname = null;

    private static void printreglog() {
        System.out.println("=== 请输入操作编号：1.注册  2.登录  3.退出系统 ===");
    }

    private static void printMaue() {
        System.out.println("=== 请输入操作编号：1.查询余额  2.存款  3.取款  4.转账  5.退出系统 ===");
    }

    public static void main(String[] args) throws Exception {
        ManagerInterface manager = ManagerImpl.getInstance();//取得业务对象
        Scanner scanner = new Scanner(System.in);

        /** 银行系统注册登录界面操作 */
        String flag = null;
        printreglog();//调用界面
        while (true) {
            System.out.println("请你输入操作: ");
            flag = scanner.next();
            if ("1".equals(flag)) {
                System.out.println("请输入用户名:");
                String uname = scanner.next();//输入用户名
                System.out.println("请输入密码:");
                String upass = scanner.next();//输入密码

                boolean reg = manager.register(uname, upass);
                if (!reg) {
                    System.out.println("该用户已存在！请重新注册");
                    printreglog();
                    continue;
                }
                System.out.println("请选择登录");
            } else if ("2".equals(flag))//登录系统
            {
                System.out.println("请输入用户名:");
                String uname = scanner.next();//输入用户名
                System.out.println("请输入密码:");
                String upass = scanner.next();//输入密码
                boolean flg = manager.login(uname, upass);
                if (!flg) {
                    System.out.println("登录失败，请重新登录:");
                    printreglog();
                    continue;
                }
                if (flg) {
                    logname = uname;
                    break;
                }
            } else if ("3".equals(flag)) {//退出系统
                System.out.println("系统已经退出");
                System.exit(1);

            } else {
                System.out.println("请你输入正确的操作");
            }
            printreglog();
        }


        /* 银行系统主界面操作 */
        flag = null;
        printMaue();//调用界面
        while (true) {

            System.out.println("请你输入操作");
            flag = scanner.next();
            if ("1".equals(flag))//查询余额
            {
                System.out.println("您当前余额为" + manager.inquiry());
            } else if ("2".equals(flag))//存    款
            {
                System.out.println("请您要存入的金额为");
                String money = scanner.next();
                //double money = scanner.nextDouble();
                try {
                    manager.deposit(money);
                    System.out.println("存款成功!您当前余额为" + manager.inquiry());
                } catch (InvalidDepositException e) {
                    System.out.println(e.getMessage());//获取异常信息
                    System.out.println("请重新输入");
                } catch (NumberFormatException e) {
                    System.out.println("输入数据格式错误，请输入数字字符");//获取异常信息
                    System.out.println("请重新输入");
                }
            } else if ("3".equals(flag))//取    款
            {
                System.out.println("请您要取出的金额为");
                String money = scanner.next();
                try {
                    if ('-' == money.charAt(0)) throw new Exception("取款金额不能为负数");
                    manager.withdrawals(money);//完成取款功能
                    System.out.println("取款成功!您当前余额为" + manager.inquiry());
                } catch (AccountOverDrawnException e) {
                    System.out.println(e.getMessage());//获取异常信息
                    System.out.println("请重新输入");
                } catch (NumberFormatException e) {
                    System.out.println("输入数据格式错误，请输入数字字符");//获取异常信息
                    System.out.println("请重新输入");
                } catch (Exception e) {
                    System.out.println(e.getMessage());//获取异常信息
                    System.out.println("请重新输入");
                }

            } else if ("4".equals(flag))//转账
            {
                System.out.println("请您输入转账的金额为：");
                String money = scanner.next();

                System.out.println("请您输入转入账号：");
                String inAccount = scanner.next();

                try {
                    if ('-' == money.charAt(0)) throw new Exception("转账金额不能为负数");
                    boolean flag1 = manager.tranferMoney(inAccount, money);//完成转账功能
                    if (flag1 = true)
                        System.out.println("转账成功");
                } catch (AccountOverDrawnException e) {
                    System.out.println(e.getMessage());//获取异常信息
                    System.out.println("请重新输入");
                } catch (NumberFormatException e) {
                    System.out.println("输入数据格式错误，请输入数字字符");//获取异常信息
                    System.out.println("请重新输入");
                } catch (IOException e) {
                    System.out.println("读取文件出错");//获取异常信息
                    System.out.println("请重新输入");
                } catch (Exception e) {
                    System.out.println(e.getMessage());//获取异常信息
                    System.out.println("请重新输入");
                }

            } else if ("5".equals(flag)) {//退出系统
                manager.exitSystem(logname);
            } else {
                System.out.println("请你输入正确的操作");
            }
            printMaue();
        }
    }
}
