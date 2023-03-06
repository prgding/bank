package dao.impl;

import dao.ActDao;
import org.apache.ibatis.session.SqlSession;
import pojo.User;
import utils.SqlSessionUtil;

public class ActDaoImpl implements ActDao {
	@Override
	public int update(User user) {
		SqlSession sqlSession = SqlSessionUtil.open();
		int count = sqlSession.update("update", user);
		System.out.println("更新条数 = " + count);
		return count;
	}

	@Override
	public User select(String username) {
		SqlSession sqlSession = SqlSessionUtil.open();
		return sqlSession.selectOne("select", username);
	}
}
