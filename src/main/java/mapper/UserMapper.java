package mapper;

import pojo.User;

public interface UserMapper {
	int update(User user);

	User select(String username);
}
