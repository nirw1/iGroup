package rest_api_tests.instance_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import iob.tests_helpers.TestingDaoService;
import iob.tests_helpers.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class UpdateInstanceTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private UserBoundary user;
	private InstanceBoundary instance;
	private InstanceBoundary newInstance;

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

	@BeforeEach
	public void before() {
		this.user = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.instance = this.testingFactory.createNewInstance(this.user.getUserId(), true);
	}

	@AfterEach
	public void after() {
		this.testingService.getInstanceDao().deleteAll();
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminUpdate() {
		UserBoundary updatingUser = this.testingFactory.createNewUser(UserRole.ADMIN);
		this.newInstance = this.testingFactory.createNewInstance(this.user.getUserId(), false);
		this.newInstance.setInstanceId(null);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.put(this.url + updatingUser + this.instance, this.newInstance);
		});
	}

	@Test
	public void testManagerUpdate() {
		UserBoundary updatingUser = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.newInstance = this.testingFactory.createNewInstance(updatingUser.getUserId(), false);
		this.newInstance.setInstanceId(null);

		this.client.put(this.url + updatingUser + this.instance, this.newInstance);

		this.newInstance = this.client.getForObject(this.url + this.user + this.instance, InstanceBoundary.class);

		assertThat(this.instance.getInstanceId().equals(this.newInstance.getInstanceId())).isTrue();
		assertThat(this.instance.getCreatedTimestamp().equals(this.newInstance.getCreatedTimestamp())).isTrue();
		assertThat(this.instance.getCreatedBy().equals(this.newInstance.getCreatedBy())).isTrue();

		assertThat(this.instance.getActive().equals(this.newInstance.getActive())).isFalse();
		assertThat(this.instance.getLocation().equals(this.newInstance.getLocation())).isFalse();
		assertThat(this.instance.getName().equals(this.newInstance.getName())).isFalse();
		assertThat(this.instance.getType().equals(this.newInstance.getType())).isFalse();

	}

	@Test
	public void testPlayerUpdate() {
		UserBoundary updatingUser = this.testingFactory.createNewUser(UserRole.PLAYER);
		this.newInstance = this.testingFactory.createNewInstance(this.user.getUserId(), false);
		this.newInstance.setInstanceId(null);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.put(this.url + updatingUser + this.instance, this.newInstance);
		});
	}

	@Test
	public void testNonExistingUser() {
		UserBoundary updatingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER, "AVATAR",
				"USERNAME");
		this.newInstance = this.testingFactory.createNewInstance(this.user.getUserId(), false);
		this.newInstance.setInstanceId(null);

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.put(this.url + updatingUser + this.instance, this.newInstance);
		});
	}
}
