package iob.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.attributes.CreatedBy;
import iob.attributes.InstanceId;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.logic.InstancesService;

@RestController
public class InstanceController {
	private InstancesService instancesService;

	@Autowired
	public InstanceController(InstancesService instancesService) {
		this.instancesService = instancesService;
	}

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary createInstance(@RequestBody InstanceBoundary instanceFromClient,
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		return this.instancesService.createInstance(userDomain, userEmail, instanceFromClient);
	}

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public void updateInstance(@RequestBody InstanceBoundary instanceFromClient,
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("instanceDomain") String instanceDomain, @PathVariable("instanceId") String instanceId) {
		// STUB implementation
		System.err.println("UPDATE INSTANCE - DOMAIN: " + userDomain + " USER: " + userEmail);
		System.err.println("                  DOMAIN: " + instanceDomain + " USER: " + instanceId);
	}

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary getInstance(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId) {
		return instancesService.getSpecificInstance(userDomain, userEmail, instanceDomain, instanceId);
	}

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] getAllInstances(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return instancesService.getAllInstances(userDomain, userEmail).toArray(new InstanceBoundary[0]);
	}
}
