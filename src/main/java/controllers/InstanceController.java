package controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundaries.InstanceBoundary;
import boundaries.MessageBoundary;
import integrative.InstanceId;

@RestController
public class InstanceController {

	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary createInstance(@RequestBody InstanceBoundary instanceFromClient) {
		return instanceFromClient;
	}

	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateInstance(@RequestBody InstanceBoundary instanceFromClient) {
		// STUB implementation
		System.err.println("UPDATE INSTANCE");
	}

	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}/{instanceDomain}/{instanceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary getInstance(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail, @PathVariable("instanceDomain") String instanceDomain,
			@PathVariable("instanceId") String instanceId) {
		return new InstanceBoundary();
	}

	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary[] getAllInstances(@PathVariable("userDomain") String userDomain,
			@PathVariable("userEmail") String userEmail) {
		return new InstanceBoundary[] {};
	}
}
