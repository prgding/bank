package utils;

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

	static ThreadLocal<SqlSession> local = new ThreadLocal<>();

	public static SqlSession open(){
		SqlSession sqlSession = local.get();
		if (sqlSession == null) {
			sqlSession = factory.openSession();
			local.set(sqlSession);
		}
		return sqlSession;
	}
	public static void close(SqlSession sqlSession){
		if (sqlSession != null) {
			sqlSession.close();
			local.remove();
		}
	}
}
