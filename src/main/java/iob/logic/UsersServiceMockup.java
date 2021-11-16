package iob.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iob.boundaries.UserBoundary;
import iob.converters.UserConverter;
import iob.data.UserEntity;
import iob.errors.user.UserAlreadyExistsException;
import iob.errors.user.UserNotFoundException;
import iob.errors.user.UserUpdateFailedException;

@Service
public class UsersServiceMockup implements UsersService {
	private Map<String, UserEntity> storage;
	private UserConverter converter;
	private final String emailRegex = "^(.+)@(.+)$";;

	@Autowired
	public UsersServiceMockup(UserConverter converter) {
		this.converter = converter;
	}

	@PostConstruct
	public void Init() {
		this.storage = Collections.synchronizedMap(new HashMap<>());
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {

		// checking if email address is valid
		Pattern pattern = Pattern.compile(emailRegex);
		Matcher matcher = pattern.matcher(user.getUserId().getEmail());
		// if not matching, throw exception
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid email provided");
		}

		UserEntity entityToStore = this.converter.convertToEntity(user);
		String key = this.converter.convertPropertiesToKey(entityToStore.getDomain(), entityToStore.getEmail());

		if (this.storage.containsKey(key)) {
			throw new UserAlreadyExistsException("User already exists with domain - " + entityToStore.getDomain()
					+ " and email - " + entityToStore.getEmail());
		}
		this.storage.put(key, entityToStore);

		return this.converter.convertToBoundary(entityToStore);
	}

	@Override
	public UserBoundary login(String userDomain, String userEmail) {

		String key = this.converter.convertPropertiesToKey(userDomain, userEmail);

		UserEntity entityInStorage = this.storage.get(key);
		if (entityInStorage == null) {
			throw new UserNotFoundException(
					"Could not find user by domain - " + userDomain + " and email - " + userEmail);
		}

		UserBoundary boundary = this.converter.convertToBoundary(entityInStorage);
		return boundary;
	}

	@Override
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary updatedUser) {

		String key = this.converter.convertPropertiesToKey(userDomain, userEmail);
		UserEntity existing = this.storage.get(key);

		if (existing == null) {
			throw new UserNotFoundException(
					"Could not find user by domain - " + userDomain + " and email - " + userEmail);
		}

		UserEntity updatedProperties = this.converter.convertToEntity(updatedUser);

		boolean dirty = false;
		// if entity exists update only non null fields (that are not domain or email)
		// from updatedUser
		if (updatedProperties.getRole() != null) {
			existing.setRole(updatedProperties.getRole());
			dirty = true;
		}

		if (updatedProperties.getUsername() != null) {
			existing.setUsername(updatedProperties.getUsername());
			dirty = true;
		}

		if (updatedProperties.getAvatar() != null) {
			existing.setAvatar(updatedProperties.getAvatar());
			dirty = true;
		}

		if (dirty) {
			this.storage.put(key, existing);

			existing = this.storage.get(key);
			if (existing == null) {
				throw new UserUpdateFailedException("User update failed with parameters:\ndomain: " + userDomain
						+ "\nemail: " + userEmail + "\nupdatedUser: " + updatedUser.toString());
			}
		}

		return this.converter.convertToBoundary(existing);
	}

	@Override
	public UserBoundary[] getAllUser(String adminDomain, String adminEmail) {
		// TODO: check if its real admin's domain and email
		return this.storage.values().stream().map(this.converter::convertToBoundary).toArray(UserBoundary[]::new);

	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		// TODO: check if its real admin's domain and email
		this.storage.clear();
	}

}
