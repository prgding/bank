package service.Impl;

import dao.ActDao;
import dao.impl.ActDaoImpl;
import exceptions.MoneyNotEnough;
import exceptions.UnknownException;
import org.apache.ibatis.session.SqlSession;
import pojo.User;
import service.AccountService;
import utils.SqlSessionUtil;

public class AccountServiceImpl implements AccountService {

	private ActDao actDao = new ActDaoImpl();

	@Override
	public void transfer(String from, String to, double money) throws MoneyNotEnough, UnknownException {

		// 事务控制
		SqlSession sqlSession = SqlSessionUtil.open();

		// 余额是否充足
		User fromUser = actDao.select(from);
		User toUser = actDao.select(to);


		// 不足
		if (fromUser.getBalance() - money < 0) {
			throw new MoneyNotEnough("余额不足");
		}
		// 充足
		fromUser.setBalance(fromUser.getBalance() - money);
		toUser.setBalance(toUser.getBalance() + money);

		// 更新数据库
		int count = actDao.update(fromUser);

		count += actDao.update(toUser);

		if (count!=2) {
			System.out.println("count =="+count);
			throw new UnknownException("未知原因失败");
		}

		sqlSession.commit();

		SqlSessionUtil.close(sqlSession);
	}
}
