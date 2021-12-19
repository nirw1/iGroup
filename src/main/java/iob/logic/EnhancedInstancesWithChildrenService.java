package iob.logic;

import java.util.List;

import iob.boundaries.InstanceBoundary;

public interface EnhancedInstancesWithChildrenService extends InstancesWithChildrenService {

	List<InstanceBoundary> getAllInstances(String userDomain, String userEmail, int page, int size);

	List<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain, String instanceId, int page, int size);

	List<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain, String instanceId,	int page, int size);

}
