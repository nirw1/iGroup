package controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundaries.ActivityBoundary;
import boundaries.UserBoundary;

@RestController
public class AdminController {

	@RequestMapping(path = "/iob/admin/users/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteUsers() {
		// STUB implementation
		System.err.println("DELETE USERS");
	}

	@RequestMapping(path = "/iob/admin/instances/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteInstances() {
		// STUB implementation
		System.err.println("DELETE INSTANCES");
	}

	@RequestMapping(path = "/iob/admin/activities/{userDomain}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteActivities() {
		// STUB implementation
		System.err.println("DELETE ACTIVITIES");
	}

	@RequestMapping(path = "/iob/admin/users/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsers() {
		UserBoundary arr[] = new UserBoundary[2];
		arr[0] = new UserBoundary(null, "role-1", "username-1", "avatar-1");
		arr[1] = new UserBoundary(null, "role-2", "username-2", "avatar-2");
		return arr;
	}

	@RequestMapping(path = "/iob/admin/activities/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary[] getActivities() {
		ActivityBoundary arr[] = new ActivityBoundary[2];
		arr[0] = new ActivityBoundary(null, "type-1", null, null);
		arr[1] = new ActivityBoundary(null, "type-2", null, null);
		return arr;
	}

}
