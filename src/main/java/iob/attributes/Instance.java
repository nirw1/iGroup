package iob.attributes;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(instanceId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instance other = (Instance) obj;
		return Objects.equals(instanceId, other.instanceId);
	}

}
