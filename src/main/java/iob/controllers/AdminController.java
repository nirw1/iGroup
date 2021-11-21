package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.ActivityBoundary;
import iob.boundaries.UserBoundary;
import iob.logic.ActivitiesService;
import iob.logic.InstancesService;
import iob.logic.UsersService;

@RestController
public class AdminController {

	private UsersService usersService;
	private InstancesService instancesService;
	private ActivitiesService activitiesService;

	@Autowired
	public AdminController(UsersService usersService, InstancesService instancesService,
			ActivitiesService activitiesService) {
		super();
		this.usersService = usersService;
		this.instancesService = instancesService;
		this.activitiesService = activitiesService;
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

		this.activitiesService.deleteAllActivities(userDomain, userEmail);
	}

	@RequestMapping(
			path = "/iob/admin/users/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public UserBoundary[] getUsers(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return this.usersService.getAllUsers(userDomain, userEmail).toArray(new UserBoundary[0]);
	}

	@RequestMapping(
			path = "/iob/admin/activities/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ActivityBoundary[] getActivities(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return this.activitiesService.getAllActivities(userDomain, userEmail).toArray(new ActivityBoundary[0]);
	}

}
