package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.ActivityBoundary;
import iob.boundaries.UserBoundary;
import iob.logic.InstancesService;
import iob.logic.UsersService;

@RestController
public class AdminController {

	private UsersService usersService;
	private InstancesService instancesService;

	@Autowired
	public AdminController(UsersService usersService, InstancesService instancesService) {
		super();
		this.usersService = usersService;
		this.instancesService = instancesService;
	}

	@RequestMapping(path = "/iob/admin/users/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteUsers(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		this.usersService.deleteAllUsers(userDomain, userEmail);
	}

	@RequestMapping(path = "/iob/admin/instances/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteInstances(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		this.instancesService.deleteAllInstances(userDomain, userEmail);
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
		return this.usersService.getAllUser(userDomain, userEmail);
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
