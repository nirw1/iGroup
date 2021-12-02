package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;
import iob.logic.InstancesWithChildrenService;

@RestController
public class InstanceController {
	private InstancesWithChildrenService instancesService;

	@Autowired
	public InstanceController(InstancesWithChildrenService instancesService) {
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
		return instancesService.createInstance(userDomain, userEmail, instanceFromClient);
	}

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public void updateInstance(@RequestBody InstanceBoundary instanceFromClient,
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("instanceDomain") String instanceDomain, @PathVariable("instanceId") String instanceId) {
		instancesService.updateInstance(userDomain, userEmail, instanceDomain, instanceId, instanceFromClient);
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

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/children",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public void bindChild(@RequestBody InstanceIdBoundary childInstanceFromClient,
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail,
			@PathVariable("instanceDomain") String instanceDomain, @PathVariable("instanceId") String instanceId) {
		instancesService.bindChild(userDomain, userEmail, instanceDomain, instanceId, childInstanceFromClient);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/children",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] getAllChildren(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId) {
		return instancesService.getAllChildren(userDomain, userEmail, instanceDomain, instanceId).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] getAllParents(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId) {
		return instancesService.getAllParents(userDomain, userEmail, instanceDomain, instanceId).toArray(new InstanceBoundary[0]);
	}
}
