package iob.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.ActivityBoundary;
import iob.boundaries.UserBoundary;

@RestController
public class AdminController {

	@RequestMapping(path = "/iob/admin/users/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteUsers(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println("DELETE USERS - DOMAIN: " + userDomain + " USER: " + userEmail);
	}

	@RequestMapping(path = "/iob/admin/instances/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteInstances(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println("DELETE INSTANCES - DOMAIN: " + userDomain + " USER: " + userEmail);
	}

	@RequestMapping(path = "/iob/admin/activities/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteActivities(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		// STUB implementation
		System.err.println("DELETE ACTIVITIES - DOMAIN: " + userDomain + " USER: " + userEmail);
	}

	@RequestMapping(
			path = "/iob/admin/users/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public UserBoundary[] getUsers(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		System.err.println("GET USERS - DOMAIN: " + userDomain + " USER: " + userEmail);
		UserBoundary arr[] = new UserBoundary[2];
		arr[0] = new UserBoundary(null, "role-1", "username-1", "avatar-1");
		arr[1] = new UserBoundary(null, "role-2", "username-2", "avatar-2");
		return arr;
	}

	@RequestMapping(
			path = "/iob/admin/activities/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ActivityBoundary[] getActivities(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		System.err.println("GET ACTIVITIES - DOMAIN: " + userDomain + " USER: " + userEmail);
		ActivityBoundary arr[] = new ActivityBoundary[2];
		arr[0] = new ActivityBoundary(null, null, "type-1", null, null, null);
		arr[1] = new ActivityBoundary(null, null, "type-2", null, null, null);
		return arr;
	}

}
