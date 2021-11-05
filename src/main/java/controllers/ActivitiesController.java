package controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundaries.ActivityBoundary;

@RestController
public class ActivitiesController {

	@RequestMapping(path = "/iob/activities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object createActivity(@RequestBody ActivityBoundary activityFromClient) {
		return activityFromClient;
	}

}
