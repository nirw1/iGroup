package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.attributes.UserId;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.logic.EnhancedUserService;

@RestController
public class UserRelatedController {
	private EnhancedUserService usersService;

	@Autowired
	public UserRelatedController(EnhancedUserService usersService) {
		super();
		this.usersService = usersService;
	}

	@RequestMapping(
			path = "/iob/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public UserBoundary createUser(@RequestBody NewUserBoundary newUser) {
		UserId userId = new UserId(null, newUser.getEmail()); 
		UserBoundary userBoundry = new UserBoundary(userId, newUser.getRole(), newUser.getUsername(),
				newUser.getAvatar());
		return this.usersService.createUser(userBoundry);
	}

	@RequestMapping(
			path = "/iob/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public UserBoundary loginUser(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return this.usersService.login(userDomain, userEmail);
	}

	@RequestMapping(
			path = "/iob/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public void updateUser(@RequestBody UserBoundary user, @PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		this.usersService.updateUser(userDomain, userEmail, user);
	}

}
