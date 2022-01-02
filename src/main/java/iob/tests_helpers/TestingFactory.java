package iob.tests_helpers;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import iob.attributes.CreatedBy;
import iob.attributes.Instance;
import iob.attributes.InstanceId;
import iob.attributes.Location;
import iob.attributes.UserId;
import iob.boundaries.ActivityBoundary;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;

@Component
@Profile("testing")
public class TestingFactory {
	private RestTemplate client;
	private AtomicLong id;
	private String createUserUrl;
	private String createInstanceUrl;
	private String createActivityUrl;

	public TestingFactory() {
		this.id = new AtomicLong();
		this.client = new RestTemplate();
	}

	public void setPort(int port) {
		this.createUserUrl = "http://localhost:" + port + "/iob/users";
		this.createInstanceUrl = "http://localhost:" + port + "/iob/instances/";
		this.createActivityUrl = "http://localhost:" + port + "/iob/activities/";
	}

	public UserBoundary createNewUser(UserRole role) {
		NewUserBoundary tmp = new NewUserBoundary();
		tmp.setAvatar("" + this.id.get());
		tmp.setRole(role);
		tmp.setEmail("" + this.id.get() + "@mail.com");
		tmp.setUsername("" + this.id.get());
		this.id.incrementAndGet();
		return this.client.postForObject(this.createUserUrl, tmp, UserBoundary.class);
	}

	public InstanceBoundary createNewInstance(UserId createdBy, boolean isActive) {
		InstanceBoundary tmp = new InstanceBoundary();
		tmp.setActive(isActive);
		tmp.setCreatedBy(new CreatedBy(createdBy));
		tmp.setCreatedTimestamp(new Date());
		tmp.setLocation(new Location(this.id.get(), this.id.get()));
		tmp.setName("" + this.id.get());
		tmp.setType("" + this.id.get());
		this.id.incrementAndGet();
		return this.client.postForObject(this.createInstanceUrl + createdBy, tmp, InstanceBoundary.class);
	}

	public InstanceBoundary createNewInstance(UserId createdBy, boolean isActive, Location location) {
		InstanceBoundary tmp = new InstanceBoundary();
		tmp.setActive(isActive);
		tmp.setCreatedBy(new CreatedBy(createdBy));
		tmp.setCreatedTimestamp(new Date());
		tmp.setLocation(location);
		tmp.setName("" + this.id.get());
		tmp.setType("" + this.id.get());
		this.id.incrementAndGet();
		return this.client.postForObject(this.createInstanceUrl + createdBy, tmp, InstanceBoundary.class);
	}

	public InstanceBoundary createNewInstance(UserId createdBy, boolean isActive, String name, String type) {
		InstanceBoundary tmp = new InstanceBoundary();
		tmp.setActive(isActive);
		tmp.setCreatedBy(new CreatedBy(createdBy));
		tmp.setCreatedTimestamp(new Date());
		tmp.setLocation(new Location(this.id.get(), this.id.get()));
		tmp.setName(name);
		tmp.setType(type);
		this.id.incrementAndGet();
		return this.client.postForObject(this.createInstanceUrl + createdBy, tmp, InstanceBoundary.class);
	}

	public Object createNewActivity(InstanceId instanceId, UserId invokedBy) {
		ActivityBoundary tmp = new ActivityBoundary();
		tmp.setActivityId(null);
		tmp.setCreatedTimestamp(new Date());
		tmp.setInstance(new Instance(instanceId));
		tmp.setInvokedBy(new CreatedBy(invokedBy));
		tmp.setType("" + this.id.get());
		this.id.incrementAndGet();
		return this.client.postForObject(this.createActivityUrl, tmp, Object.class);
	}

	public RestTemplate getClient() {
		return client;
	}

	public void setClient(RestTemplate client) {
		this.client = client;
	}

	public AtomicLong getId() {
		return id;
	}

	public void setId(AtomicLong id) {
		this.id = id;
	}

	public String getCreateUserUrl() {
		return createUserUrl;
	}

	public void setCreateUserUrl(String createUserUrl) {
		this.createUserUrl = createUserUrl;
	}

	public String getCreateInstanceUrl() {
		return createInstanceUrl;
	}

	public void setCreateInstanceUrl(String createInstanceUrl) {
		this.createInstanceUrl = createInstanceUrl;
	}

	public String getCreateActivityUrl() {
		return createActivityUrl;
	}

	public void setCreateActivityUrl(String createActivityUrl) {
		this.createActivityUrl = createActivityUrl;
	}

}
