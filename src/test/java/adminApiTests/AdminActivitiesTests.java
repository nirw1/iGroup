package adminApiTests;

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
import iob.boundaries.ActivityBoundary;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.ActivityEntity;
import iob.data.UserRole;
import iob.logic.TestingDaoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class AdminActivitiesTests {

	@Autowired
	private TestingDaoService testingService;

	private String createUserUrl;
	private UserId user;

	private String url;
	private int port;
	private RestTemplate client;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void postConstruct() {
		this.url = "http://localhost:" + this.port + "/iob/admin/activities/";
		this.createUserUrl = "http://localhost:" + this.port + "/iob/users/";
		this.client = new RestTemplate();
	}

	@BeforeEach
	public void createActivity() {
		ActivityEntity entity = new ActivityEntity();
		entity.setId(1);
		entity.setDomain("DOMAIN");
//		entity.setInvokedBy("USER");
		this.testingService.getActivityDao().save(entity);
	}

	@AfterEach
	public void deleteAllActivitiesAndUsers() {
		this.testingService.getUserDao().deleteAll();
		this.testingService.getActivityDao().deleteAll();
	}

	@Test
	public void testAdminDeleteAllActivities() {
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();

		this.client.delete(this.url + this.user);
		assertThat(this.testingService.getActivityDao().findAll()).hasSize(0);
	}

	@Test
	public void testManagerDeleteAllActivities() {
		this.user = this.client.postForObject(this.createUserUrl,
				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
				.getUserId();

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

	@Test
	public void testPlayerDeleteAllActivities() {
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

	@Test
	public void testAdminGetAllActivities() {
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();
		assertThat(this.client.getForObject(this.url + this.user, ActivityBoundary[].class)).hasSizeGreaterThan(0);
	}

	@Test
	public void testManagerGetAllActivities() {
		this.user = this.client.postForObject(this.createUserUrl,
				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
				.getUserId();

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + this.user, ActivityBoundary[].class);
		});
	}

	@Test
	public void testPlayerGetAllActivities() {
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + this.user, ActivityBoundary[].class);
		});
	}
}
