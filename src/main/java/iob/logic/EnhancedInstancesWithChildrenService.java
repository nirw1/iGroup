package iob.logic;

import java.util.List;

import iob.boundaries.InstanceBoundary;

public interface EnhancedInstancesWithChildrenService extends InstancesWithChildrenService {

	List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int page, int size);

	List<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain, String instanceId, int page, int size);

	List<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain, String instanceId,	int page, int size);

	List<InstanceBoundary> getByName(String userDomain, String userEmail, String name, int page, int size);

	List<InstanceBoundary> getByType(String userDomain, String userEmail, String type, int page, int size);

	List<InstanceBoundary> getByLocation(String userDomain, String userEmail, String lat, String lng, String distance, int page, int size);

	List<InstanceBoundary> getByCreationTime(String userDomain, String userEmail, String creationWindow, int page,
			int size);
	
	List<InstanceBoundary> getByNameContaining(String userDomain, String userEmail, String name, int page, int size);

}
