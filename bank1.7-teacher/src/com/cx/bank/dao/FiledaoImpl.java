package com.cx.bank.dao;

import com.cx.bank.model.MoneyBean;
import com.cx.bank.model.UserBean;
import com.cx.bank.util.MD5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 持久层实现类(文件存取）
 *
 * @author Administrator
 */
public class FiledaoImpl implements BankDaoInterface {
    MD5 md5 = new MD5();

    public FiledaoImpl() {
    }


    /**
     * 存储方法实现
     *
     * @param logname   登录帐号
     * @param MoneyBean
     * @return void
     * @throws Exception
     */
    public void saveMoney(String logname, MoneyBean moneyBean) throws Exception {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(new File(logname + ".properties"));
        props.load(fis);
        fis.close();
        props.setProperty("money", Double.toString(moneyBean.getMoney()));
        FileOutputStream fos = new FileOutputStream(logname + ".properties");
        props.store(fos, logname + ".properties");
        fos.close();
    }

    /**
     * 按名字查找用户
     *
     * @param name 用户名
     * @param psd  密码
     * @return boolean
     */

    public boolean findByName(String name) {

        File f = new File(name + ".properties");
        return !f.exists();
    }

    /**
     * 添加用户方法实现
     *
     * @param UserBean（用户名，密码）
     * @return void
     */

    public void insertUser(UserBean user) {
        FileInputStream fis = null;
        String name = user.getUserName();
        String psd = user.getPassword();
        try {
            Properties props = new Properties();
            fis = new FileInputStream(new File("Bank.properties"));
            props.load(fis);
            String upass = md5.encode(psd.getBytes());//用md5技术对用户密码进行加密
            props.setProperty("userName", name);
            props.setProperty("password", upass);
            props.setProperty("money", "10");
            FileOutputStream fos = new FileOutputStream(name + ".properties");
            props.store(fos, name + ".properties");
            fos.close();
            System.out.println("注册成功！");
        } catch (IOException e) {
            System.out.println("读取文件出错！");
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 按用户名和密码查找用户
     *
     * @param UserBean（用户名，密码）
     * @param MoneyBean（money）
     * @return boolean
     */
    public boolean findUser(UserBean user, MoneyBean moneyBean) {
        String name = user.getUserName();
        String psd = user.getPassword();
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(new File(name + ".properties"));
            props.load(fis);
            fis.close();
            String upass1 = md5.encode(psd.getBytes());//用md5技术对用户密码进行加密
            String upass2 = props.getProperty("password");//取得密码的密文
            if (name.equals(props.getProperty("userName")) && (upass1.equals(upass2))) {
                moneyBean.setMoney(Double.parseDouble(props.getProperty("money")));
                System.out.println("登录成功");
                return true;
            } else {
                System.out.println("密码错误！");
                return false;
            }
        } catch (IOException e) {
            System.out.println("用户不存在!");
            return false;
        }

    }


    /**
     * 更新余额
     *
     * @param MoneyBean(money)
     * @param inAccount        转入账号
     * @param double           dmoney 转账金额
     * @return void
     * @throws IOException
     */
    public void updateMoney(MoneyBean moneyBean, String inAccount, double dmoney) throws IOException {
        Properties props = new Properties();
        double leave = moneyBean.getMoney();
        double num = leave - dmoney;//用户的余额减去要转账的金额

        moneyBean.setMoney(num);

        /*读入要转账用户的证书信息*/
        FileInputStream in1file = new FileInputStream(inAccount + ".properties");
        props.load(in1file);
        in1file.close();//关闭文件流

        double num1 = Double.parseDouble(props.getProperty("money")) + dmoney;//要转账用户的余额加上要转账的金额
        String s1 = String.valueOf(num1);//把double类型变量转化为字符串

        props.setProperty("money", s1);//设置要转账用户的余额

        /*把改变后的要转账用户的信息重新存入文件*/
        FileOutputStream out = new FileOutputStream(inAccount + ".properties");
        props.store(out, inAccount + ".properties");
        out.close();//关闭文件流
    }


}
