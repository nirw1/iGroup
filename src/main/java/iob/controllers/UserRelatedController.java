package iob.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.attributes.UserId;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;

@RestController
public class UserRelatedController {
	@RequestMapping(
			path = "/iob/users",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public UserBoundary createUser(@RequestBody NewUserBoundary newUser) {
		UserId userId = new UserId("demo.domain", newUser.getEmail()); // TODO demo need to be changed to agreed value
		UserBoundary userBoundry = new UserBoundary(userId, newUser.getRole(), newUser.getUsername(),
				newUser.getAvatar());
		return userBoundry;
	}

	@RequestMapping(
			path = "/iob/users/login/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public UserBoundary loginUser(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		UserBoundary userBoundry = new UserBoundary();
		userBoundry.setUserId(new UserId(userDomain, userEmail));
		return userBoundry;
	}

	@RequestMapping(
			path = "/iob/users/{userDomain}/{userEmail}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public void updateUser(@RequestBody UserBoundary user, @PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println(user);
		System.err.println("UPDATE USER - DOMAIN: " + userDomain + " USER: " + userEmail);
	}

}
