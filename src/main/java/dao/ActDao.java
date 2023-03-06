package dao;

import pojo.User;

public interface ActDao {
	int update(User user);

	User select(String username);
}
