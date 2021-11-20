package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.ActivityBoundary;
import iob.logic.ActivitiesService;

@RestController
public class ActivitiesController {
	private ActivitiesService activitiesService;

	@Autowired
	public ActivitiesController(ActivitiesService activitiesService) {
		super();
		this.activitiesService = activitiesService;
	}

	@RequestMapping(path = "/iob/activities", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Object createActivity(@RequestBody ActivityBoundary activityFromClient) {
		 return this.activitiesService.invokeActivity(activityFromClient);
	}

}
