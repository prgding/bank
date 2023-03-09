package me.ding.mapper;

import me.ding.pojo.User;

public interface UserMapper {
	int update(User user);

	User select(String username);
}
