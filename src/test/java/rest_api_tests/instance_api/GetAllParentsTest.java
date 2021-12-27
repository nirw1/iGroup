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
import iob.boundaries.InstanceIdBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.tests_helpers.TestingDaoService;
import iob.tests_helpers.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class GetAllParentsTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private UserBoundary user;
	private InstanceBoundary activeParentInstance;
	private InstanceBoundary inActiveParentInstance;
	private InstanceBoundary childInstance;

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
		this.activeParentInstance = this.testingFactory.createNewInstance(this.user.getUserId(), true);
		this.inActiveParentInstance = this.testingFactory.createNewInstance(this.user.getUserId(), false);
		this.childInstance = this.testingFactory.createNewInstance(this.user.getUserId(), true);

		this.client.put(this.url + this.user + this.activeParentInstance + "/children", new InstanceIdBoundary(
				this.childInstance.getInstanceId().getDomain(), this.childInstance.getInstanceId().getId()));
		this.client.put(this.url + this.user + this.inActiveParentInstance + "/children", new InstanceIdBoundary(
				this.childInstance.getInstanceId().getDomain(), this.childInstance.getInstanceId().getId()));
	}

	@AfterEach
	public void after() {
		this.testingService.getInstanceDao().deleteAll();
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminGetParents() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + requestingUser + this.childInstance + "/parents",
					InstanceBoundary[].class);
		});
	}

	@Test
	public void testManagerGetParents() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		assertThat(this.client.getForObject(this.url + requestingUser + this.childInstance + "/parents",
				InstanceBoundary[].class)).hasSize(2);
	}

	@Test
	public void testPlayerGetParents() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		assertThat(this.client.getForObject(this.url + requestingUser + this.childInstance + "/parents",
				InstanceBoundary[].class)).hasSize(1).allMatch(instance -> instance.getActive() == true);
	}

	@Test
	public void testNonExistingUserGetParents() {
		UserBoundary requestingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER,
				"AVATAR", "USERNAME");

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.getForObject(this.url + requestingUser + this.childInstance + "/parents",
					InstanceBoundary[].class);
		});
	}

}
