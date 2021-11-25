package iob.logic;

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

import iob.attributes.UserId;
import iob.boundaries.UserBoundary;
import iob.converters.UserConverter;
import iob.daos.UserDao;
import iob.data.UserEntity;
import iob.errors.BadRequestException;
import iob.errors.NotFoundException;

@Service
public class UsersLogicJpa implements UsersService {
	private UserDao userDao;
	private UserConverter converter;
	private final String emailRegex = "^(.+)@(.+)$";
	private String appName;

	@Autowired
	public UsersLogicJpa(UserDao userDao, UserConverter converter) {
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
	public List<UserBoundary> getAllUsers(String adminDomain, String adminEmail) {
		return StreamSupport.stream(this.userDao.findAll().spliterator(), false).map(this.converter::convertToBoundary)
				.collect(Collectors.toList());
	}

	@Override
	public void deleteAllUsers(String adminDomain, String adminEmail) {
		this.userDao.deleteAll();
	}

}
