package com.cx.bank.factory;

import com.cx.bank.dao.FileDaoInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * UserDaoFactory
 * 通过反射创建对象
 *
 * @author dingshuai
 * @version 1.6
 */
public class UserDaoFactory {
    private static UserDaoFactory instance = new UserDaoFactory();
    private FileDaoInterface dao;

    private UserDaoFactory() {
        try {
            // 1.读取 properties 文件
            Properties props = new Properties();
            InputStream is = getClass().getResourceAsStream("/classInfo.properties");
            props.load(is);
            // 2.获取 className
            String className = props.getProperty("className");
            // 3.通过反射创建对象
            dao = (FileDaoInterface) Class.forName(className).newInstance();
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserDaoFactory getInstance() {
        return instance;
    }

    public FileDaoInterface getDao() {
        return dao;
    }
}
