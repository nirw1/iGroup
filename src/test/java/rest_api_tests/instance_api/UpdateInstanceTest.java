package rest_api_tests.instance_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import iob.Application;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.TestingDaoService;
import iob.logic.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class UpdateInstanceTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private RestTemplate client;
	private String url;
	private int port;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void postConstruct() {
		this.client = new RestTemplate();
		this.testingFactory.setPort(this.port);
		this.url = "http://localhost:" + this.port + "/iob/instances/";
	}

	@AfterEach
	public void after() {
		this.testingService.getInstanceDao().deleteAll();
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminUpdate() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary instance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary newInstance = this.testingFactory.createNewInstance(user.getUserId(), false);
		newInstance.setInstanceId(null);

		UserBoundary updatingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.put(this.url + updatingUser + instance, newInstance);
		});
	}

	@Test
	public void testManagerUpdate() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		UserBoundary updatingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		InstanceBoundary instance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary newInstance = this.testingFactory.createNewInstance(updatingUser.getUserId(), false);
		newInstance.setInstanceId(null);

		this.client.put(this.url + updatingUser + instance, newInstance);

		newInstance = this.client.getForObject(this.url + user + instance, InstanceBoundary.class);

		assertThat(instance.getInstanceId().equals(newInstance.getInstanceId())).isTrue();
		assertThat(instance.getCreatedTimestamp().equals(newInstance.getCreatedTimestamp())).isTrue();
		assertThat(instance.getCreatedBy().equals(newInstance.getCreatedBy())).isTrue();

		assertThat(instance.getActive().equals(newInstance.getActive())).isFalse();
		assertThat(instance.getLocation().equals(newInstance.getLocation())).isFalse();
		assertThat(instance.getName().equals(newInstance.getName())).isFalse();
		assertThat(instance.getType().equals(newInstance.getType())).isFalse();

	}

	@Test
	public void testPlayerUpdate() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary instance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary newInstance = this.testingFactory.createNewInstance(user.getUserId(), false);

		UserBoundary updatingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.put(this.url + updatingUser + instance, newInstance);
		});
	}

	@Test
	public void testNonExistingUser() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary instance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary newInstance = this.testingFactory.createNewInstance(user.getUserId(), false);

		UserBoundary updatingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER, "AVATAR",
				"USERNAME");
		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.put(this.url + updatingUser + instance, newInstance);
		});
	}
}
