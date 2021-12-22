package iob.logic;

import iob.data.UserRole;

public interface EnhancedUserService extends UsersService {

	public UserRole getUserRole(String domain, String email);

}
