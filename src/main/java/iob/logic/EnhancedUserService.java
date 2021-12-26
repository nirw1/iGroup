package iob.logic;

import java.util.List;

import iob.boundaries.UserBoundary;
import iob.data.UserRole;

public interface EnhancedUserService extends UsersService {
	
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail, int page, int size);

	public UserRole getUserRole(String domain, String email);

}
