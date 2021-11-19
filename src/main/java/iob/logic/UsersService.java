package iob.logic;

import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;

public interface UsersService {

	public UserBoundary createUser(NewUserBoundary user);

	public UserBoundary login(String userDomain, String userEmail);

	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary uptade);

	public UserBoundary[] getAllUser(String adminDomain, String adminEmail);

	public void deleteAllUsers(String adminDomain, String adminEmail);

}
