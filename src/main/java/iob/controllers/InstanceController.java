package iob.controllers;

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

@RestController
public class InstanceController {

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}",
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary createInstance(@RequestBody InstanceBoundary instanceFromClient,
			@PathVariable("userDomain") String userDomain, @PathVariable("userEmail") String userEmail) {
		instanceFromClient.getInstanceId().setDomain(userDomain);
		instanceFromClient.setCreatedBy(new CreatedBy(new UserId(userDomain, userEmail)));
		return instanceFromClient;
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
		InstanceBoundary tmp = new InstanceBoundary();
		tmp.setCreatedBy(new CreatedBy(new UserId(userDomain, userEmail)));
		tmp.setInstanceId(new InstanceId(instanceDomain, instanceId));
		return tmp;
	}

	@RequestMapping(
			path = "/iob/instances/{userDomain}/{userEmail}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public InstanceBoundary[] getAllInstances(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		InstanceBoundary arr[] = new InstanceBoundary[2];
		arr[0] = new InstanceBoundary(new InstanceId(), "type-1", "name-1", true, null, new CreatedBy(new UserId(userDomain,userEmail)), null, null);
		arr[1] = new InstanceBoundary(new InstanceId(), "type-2", "name-2", true, null, new CreatedBy(new UserId(userDomain,userEmail)), null, null);
		return arr;
	}
}
