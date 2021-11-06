package controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import boundaries.ActivityBoundary;
import boundaries.InstanceBoundary;
import boundaries.MessageBoundary;
import integrative.CreatedBy;
import integrative.InstanceId;
import integrative.Location;

@RestController
public class InstanceController {

	@RequestMapping(path = "/iob/instances/{userDomain}/{userEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public InstanceBoundary createInstance(@RequestBody InstanceBoundary instanceFromClient) {
		// Create InstanceBoundary, then return it
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
		InstanceBoundary arr[] = new InstanceBoundary[2];
		arr[0] = new InstanceBoundary(null, "type-1", "name-1", true, null, null, null, null);
		arr[1] = new InstanceBoundary(null, "type-2", "name-2", true, null, null, null, null);
		return arr;
	}
}
