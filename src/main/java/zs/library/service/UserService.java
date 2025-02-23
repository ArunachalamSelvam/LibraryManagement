package zs.library.service;

import zs.library.model.User;

public interface UserService {
	User addUser(User user) throws ClassNotFoundException;

	boolean isUserExists(String email) throws ClassNotFoundException;

	User getUserByEmail(String email) throws ClassNotFoundException;
}
