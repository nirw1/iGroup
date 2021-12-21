package iob.logic;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iob.annotations.RolePermission;
import iob.attributes.UserId;
import iob.boundaries.UserBoundary;
import iob.converters.UserConverter;
import iob.daos.UserDao;
import iob.data.UserEntity;
import iob.data.UserRole;
import iob.errors.BadRequestException;
import iob.errors.NotFoundException;
import iob.errors.UserAlreadyExistsException;

@Service
public class UsersServiceJpa implements EnhancedUserService {
	private UserDao userDao;
	private UserConverter converter;
	private final String emailRegex = "^(.+)@(.+)$";
	private String appName;

	@Autowired
	public UsersServiceJpa(UserDao userDao, UserConverter converter) {
		this.userDao = userDao;
		this.converter = converter;
	}

	@Value("${spring.application.name}") // read this value from Spring Configuration
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	@Transactional // (readOnly = false)
	public UserBoundary createUser(UserBoundary user) {
		user.getUserId().setDomain(appName);
		Optional<UserEntity> op = this.userDao.findById(user.getUserId());
		if (op.isPresent()) {
			throw new UserAlreadyExistsException(
					user.getUserId().getEmail() + " already exist in domain " + user.getUserId().getDomain());
		} else {

			// checking if email address is valid
			Pattern pattern = Pattern.compile(emailRegex);
			Matcher matcher = pattern.matcher(user.getUserId().getEmail());

			// if not matching, throw exception
			if (!matcher.matches()) {
				throw new RuntimeException("Invalid email provided");
			}

			if (user.getUsername() == null || user.getUsername().isEmpty()) {
				throw new BadRequestException("username cannot be empty or null");
			}

			if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
				throw new BadRequestException("avatar cannot be empty or null");
			}

			UserEntity entityToStore = this.converter.convertToEntity(user);
			entityToStore.setDomain(appName);
			this.userDao.save(entityToStore);
			return this.converter.convertToBoundary(entityToStore);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userDomain, String userEmail) {
		Optional<UserEntity> op = this.userDao.findById(new UserId(userDomain, userEmail));
		if (op.isPresent()) {
			UserEntity entity = op.get();
			return this.converter.convertToBoundary(entity);
		} else {
			throw new NotFoundException("User " + userEmail + " not found in domain " + userDomain);
		}

	}

	@Override
	@Transactional
	public UserBoundary updateUser(String userDomain, String userEmail, UserBoundary update) {
		Optional<UserEntity> op = this.userDao.findById(new UserId(userDomain, userEmail));
		if (op.isPresent()) {
			UserEntity entity = op.get();
			UserEntity updatedProperties = this.converter.convertToEntity(update);

			if (updatedProperties.getRole() != null) {
				entity.setRole(updatedProperties.getRole());
			}

			if (updatedProperties.getUsername() != null && updatedProperties.getUsername().isEmpty() == false) {
				entity.setUsername(updatedProperties.getUsername());
			}

			if (updatedProperties.getAvatar() != null && updatedProperties.getAvatar().isEmpty() == false) {
				entity.setAvatar(updatedProperties.getAvatar());
			}

			return this.converter.convertToBoundary(this.userDao.save(entity));
		} else {
			throw new NotFoundException("User " + userEmail + " not found in domain " + userDomain);
		}

	}

	@Override
	@Transactional(readOnly = true)
	@RolePermission(roles = { UserRole.ADMIN })
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		return StreamSupport.stream(this.userDao.findAll().spliterator(), false).map(this.converter::convertToBoundary)
				.collect(Collectors.toList());

	}

	@Override
	@Transactional
	@RolePermission(roles = { UserRole.ADMIN })
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		this.userDao.deleteAll();
	}

	@Override
	public UserRole getUserRole(String domain, String email) {
		UserRole role = null;
		try {
			role = StreamSupport.stream(this.userDao.findAll().spliterator(), false)
					.filter(user -> user.getDomain().equals(domain) && user.getEmail().equals(email))
					.map(this.converter::convertToBoundary).collect(Collectors.toList()).get(0).getRole();
		} catch (IndexOutOfBoundsException e) {
			throw new NotFoundException(email + " doesn't exist in domain" + domain);
		} catch (Exception e) {
			throw new RuntimeException("Error");
		}
		return role;
	}

}
