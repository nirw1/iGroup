package iob.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;
import iob.logic.EnhancedInstancesWithChildrenService;

@RestController
public class InstanceController {
	private EnhancedInstancesWithChildrenService instancesService;

	@Autowired
	public InstanceController(EnhancedInstancesWithChildrenService instancesService) {
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
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getAllInstances(userDomain, userEmail, page, size).toArray(new InstanceBoundary[0]);
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
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getAllChildren(userDomain, userEmail, instanceDomain, instanceId, page, size).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}/parents",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] getAllParents(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getAllParents(userDomain, userEmail, instanceDomain, instanceId, page, size).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/search/byName/{name}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] searchByName(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("name") String name,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getByName(userDomain, userEmail, name, page, size).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/search/byNameContaining/{name}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] searchByNameContaining(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("name") String name,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getByNameContaining(userDomain, userEmail, name, page, size).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/search/byType/{type}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] searchByType(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("type") String type,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getByType(userDomain, userEmail, type, page, size).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/search/near/{lat}/{lng}/{distance}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] searchByLocation(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("lat") String lat,
			@PathVariable("lng") String lng, @PathVariable("distance") String distance,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getByLocation(userDomain, userEmail, lat, lng, distance, page, size).toArray(new InstanceBoundary[0]);
	}
	
	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}/search/created/{creationWindow}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] searchByCreationTime(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("creationWindow") String creationWindow,
			@RequestParam(name="page", required=false, defaultValue = "0") int page,
			@RequestParam(name="size", required=false, defaultValue = "10") int size) {
		return instancesService.getByCreationTime(userDomain, userEmail, creationWindow, page, size).toArray(new InstanceBoundary[0]);
	}
}
