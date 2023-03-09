package me.ding.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class SqlSessionUtil {
	private static SqlSessionFactory factory;
	static {
		try {
			factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static ThreadLocal<SqlSession> local = new ThreadLocal<>();

	public static SqlSession open(){
		System.out.println("open方法");
		SqlSession sqlSession = local.get();
		if (sqlSession == null) {
			System.out.println("sqlSession为空，新建一个");
			sqlSession = factory.openSession();
			local.set(sqlSession);
		}
		return sqlSession;
	}
	public static void close(SqlSession sqlSession){
		System.out.println("close方法");
		if (sqlSession != null) {
			System.out.println("close不为空，关闭");
			sqlSession.close();
			local.remove();
		}
	}
}
