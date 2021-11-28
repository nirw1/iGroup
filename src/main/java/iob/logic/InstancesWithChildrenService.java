package iob.logic;

import java.util.List;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;

public interface InstancesWithChildrenService extends InstancesService {

	public void bindChild(String userDomain, String userEmail, String instanceDomain, String instanceId, InstanceIdBoundary child);

	public List<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain, String instanceId);

	public List<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain, String instanceId);
}
