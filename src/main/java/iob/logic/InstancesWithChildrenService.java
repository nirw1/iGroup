package iob.logic;

import java.util.Set;

import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;

public interface InstancesWithChildrenService extends InstancesService {

	public void bindChild(String userDomain, String userEmail, String instanceDomain, String instanceId, InstanceIdBoundary child);

	@Deprecated
	public Set<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain, String instanceId);

	@Deprecated
	public Set<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain, String instanceId);
}
