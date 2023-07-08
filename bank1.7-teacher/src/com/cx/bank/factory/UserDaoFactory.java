package com.cx.bank.factory;

import com.cx.bank.dao.BankDaoInterface;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * 工厂类，负责动态装载FiledaoImpl类
 */
public class UserDaoFactory {

    private static UserDaoFactory instance;

    private final BankDaoInterface userDao;

    private UserDaoFactory() throws Exception {

        Properties p = new Properties();//创建属性对象
        FileInputStream fis = new FileInputStream(new File("classInfo.properties"));//创建流对象
        p.load(fis);//加载
        fis.close();//关闭流

        String className = p.getProperty("className");//通过key获取value获取类文件名
        Class c = Class.forName(className);//加载类创建映射对象
        Object o = c.newInstance();//创建对象

        //注意:从classInfo.properties配置文件中动态装载持久层FiledaoImpl实现类,便于灵活更换
        userDao = (BankDaoInterface) o;


    }

    public static synchronized UserDaoFactory getInstance() throws Exception {
        if (instance == null) {
            instance = new UserDaoFactory();
        }
        return instance;
    }

    /**
     * 创建UserDao对象
     *
     * @return UserDao UserDao接口
     */
    public BankDaoInterface createUserDao() {
        return userDao;
    }
}
