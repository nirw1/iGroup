package iob.logic;

import java.util.List;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.InstanceIdBoundary;

public interface InstancesService {
	public InstanceBoundary createInstance(String userDomain, String userEmail, InstanceBoundary instance);

	public InstanceBoundary updateInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId, InstanceBoundary update);

	public List<InstanceBoundary> getAllInstances(String userDomain, String userEmail);

	public InstanceBoundary getSpecificInstance(String userDomain, String userEmail, String instanceDomain,
			String instanceId);

	public void deleteAllInstances(String adminDomain, String adminEmail);

	public void bindChild(String userDomain, String userEmail, String instanceDomain, String instanceId, InstanceIdBoundary child);

	public List<InstanceBoundary> getAllChildren(String userDomain, String userEmail, String instanceDomain, String instanceId);

	public List<InstanceBoundary> getAllParents(String userDomain, String userEmail, String instanceDomain, String instanceId);
}
