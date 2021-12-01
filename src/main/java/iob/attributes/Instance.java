package iob.attributes;

import iob.boundaries.InstanceIdBoundary;

public class Instance {
	private InstanceIdBoundary instanceId = null;

	public Instance() {

	}

	public Instance(InstanceIdBoundary instanceId) {
		super();
		this.instanceId = instanceId;
	}

	public InstanceIdBoundary getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(InstanceIdBoundary instanceId) {
		this.instanceId = instanceId;
	}

	public String domain() {
		if (this.instanceId != null) {
			return this.instanceId.getDomain();
		}
		return null;
	}

	public String id() {
		if (this.instanceId != null) {
			return this.instanceId.getId();
		}
		return null;
	}

}
