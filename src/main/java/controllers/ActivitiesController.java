package controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import boundaries.ActivityBoundary;

public class ActivitiesController {

	@RequestMapping(path = "/iob/activities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ActivityBoundary createActivity(@RequestBody ActivityBoundary activityFromClient) {
		return activityFromClient;
	}

}
