package me.ding.service.Impl;

import me.ding.exceptions.MoneyNotEnough;
import me.ding.exceptions.UnknownException;
import me.ding.mapper.UserMapper;
import me.ding.pojo.User;
import me.ding.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("accountService")
public class AccountServiceImpl implements AccountService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public void transfer(String from, String to, double money) throws MoneyNotEnough, UnknownException {
		// 余额是否充足
		User fromUser = userMapper.select(from);
		User toUser = userMapper.select(to);

		// 不足
		if (fromUser.getBalance() - money < 0) {
			throw new MoneyNotEnough("余额不足");
		}
		// 充足
		fromUser.setBalance(fromUser.getBalance() - money);
		toUser.setBalance(toUser.getBalance() + money);

		// 更新数据库
		int count = userMapper.update(fromUser);
		String s = null;
		s.toString();
		count += userMapper.update(toUser);

		if (count!=2) {
			System.out.println("count ==" + count);
			throw new UnknownException("未知原因失败");
		}
	}
}