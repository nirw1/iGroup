package iob.attributes;

public class Instance {
	private InstanceId instanceId = null;

	public Instance() {

	}

	public Instance(InstanceId instanceId) {
		super();
		this.instanceId = instanceId;
	}

	public InstanceId getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(InstanceId instanceId) {
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
