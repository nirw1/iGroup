package rest_api_tests.activity_api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import iob.attributes.CreatedBy;
import iob.attributes.Instance;
import iob.attributes.UserId;
import iob.boundaries.ActivityBoundary;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.tests_helpers.TestingDaoService;
import iob.tests_helpers.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class InvokeActivityTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private UserBoundary user;
	private InstanceBoundary instance;

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
		this.url = "http://localhost:" + this.port + "/iob/activities/";
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
		this.testingService.getActivityDao().deleteAll();
	}

	@Test
	public void testAdminInvokeActivity() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		ActivityBoundary activity = new ActivityBoundary();
		activity.setActivityId(null);
		activity.setInstance(new Instance(this.instance.getInstanceId()));
		activity.setInvokedBy(new CreatedBy(requestingUser.getUserId()));
		activity.setType("Activity");

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.postForObject(this.url, activity, Object.class);
		});
	}

	@Test
	public void testManagerInvokeActivity() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		ActivityBoundary activity = new ActivityBoundary();
		activity.setActivityId(null);
		activity.setInstance(new Instance(this.instance.getInstanceId()));
		activity.setInvokedBy(new CreatedBy(requestingUser.getUserId()));
		activity.setType("Activity");

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.postForObject(this.url, activity, Object.class);
		});
	}

	@Test
	public void testPlayerInvokeActivity() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		ActivityBoundary activity = new ActivityBoundary();
		activity.setActivityId(null);
		activity.setInstance(new Instance(this.instance.getInstanceId()));
		activity.setInvokedBy(new CreatedBy(requestingUser.getUserId()));
		activity.setType("Activity");

		assertDoesNotThrow(() -> {
			this.client.postForObject(this.url, activity, Object.class);
		});
		assertThat(this.testingService.getActivityDao().findAll()).hasSize(1);
	}

	@Test
	public void testNonExistingUserInvokeActivity() {
		UserBoundary requestingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER,
				"AVATAR", "USERNAME");
		ActivityBoundary activity = new ActivityBoundary();
		activity.setActivityId(null);
		activity.setInstance(new Instance(this.instance.getInstanceId()));
		activity.setInvokedBy(new CreatedBy(requestingUser.getUserId()));
		activity.setType("Activity");

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.postForObject(this.url, activity, Object.class);
		});
	}

}
