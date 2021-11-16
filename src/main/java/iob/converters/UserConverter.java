package iob.converters;

import org.springframework.stereotype.Component;

import iob.attributes.UserId;
import iob.boundaries.UserBoundary;
import iob.data.UserEntity;
import iob.data.UserRole;

@Component
public class UserConverter {
	
	public UserBoundary convertToBoundary(UserEntity entity) {
		UserBoundary boundary = new UserBoundary();
		UserId userId = new UserId(entity.getDomain(), entity.getEmail());
		boundary.setUserId(userId);
		boundary.setUsername(entity.getUsername());
		boundary.setRole(convertStringToUserRole(entity.getRole()));
		boundary.setAvatar(entity.getAvatar());
		return boundary;
	}
	
	public UserEntity convertToEntity(UserBoundary boundary) {
		UserEntity entity = new UserEntity();
		UserId userId = boundary.getUserId();
		if(userId!=null) {
			entity.setDomain(userId.getDomain());
			entity.setEmail(userId.getEmail());
		} else {
			entity.setDomain(null);
			entity.setEmail(null);
		}
		
		entity.setRole(convertUserRoleToString(boundary.getRole()));
		entity.setUsername(boundary.getUsername());
		entity.setAvatar(boundary.getAvatar());
		return entity;
	}
	
	public String convertPropertiesToKey(String domain, String email) {
		return domain + "#" + email;
	}
	
	public UserRole convertStringToUserRole(String role) {
		if(role == null) {
			return null;
		}
		return UserRole.valueOf(role);
	}
	
	public String convertUserRoleToString(UserRole userRole) {
		if(userRole == null) {
			return UserRole.PLAYER.name();
		}
		return userRole.name();
	}
	
	

}
