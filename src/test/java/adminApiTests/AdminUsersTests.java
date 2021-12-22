package adminApiTests;

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
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.TestingDaoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class AdminUsersTests {

	@Autowired
	private TestingDaoService testingDaoService;

	private String createUserUrl;
	private UserId user;

	private int port;
	private RestTemplate client;
	private String url;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void initTestCase() {
		this.url = "http://localhost:" + this.port + "/iob/admin/users/";
		this.createUserUrl = "http://localhost:" + this.port + "/iob/users";
		this.client = new RestTemplate();

	}

	@AfterEach
	public void deleteAllUsers() {
		testingDaoService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminDeleteAllUsers() {
		// Given server is up & user list size greater than 0
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();

		// When Admin delete all users
		this.client.delete(this.url + this.user);

		// Then users list size equals 0
		assertThat(this.testingDaoService.getUserDao().findAll()).hasSize(0);
	}

	@Test
	public void testManagerDeleteAllUsers() {
		// Given server is up & user list size greater than 0

		this.user = this.client.postForObject(this.createUserUrl,
				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
				.getUserId();

		// When Manager delete all users
		// HttpClientErrorException should occur
		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

	@Test
	public void testPlayerDeleteAllUsers() {
		// Given server is up & user list size greater than 0
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();

		// When Player delete all users
		// HttpClientErrorException should occur
		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

	@Test
	public void testAdminGetAllUsers() {
		// Given server is up & user list size greater than 0
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();

		// When Admin export all users
		// Then return list size is greater than 0
		assertThat(this.client.getForObject(this.url + this.user, UserBoundary[].class)).hasSizeGreaterThan(0);
	}

	@Test
	public void testManagerGetAllUsers() {
		// Given server is up & user list size greater than 0
		this.user = this.client.postForObject(this.createUserUrl,
				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
				.getUserId();

		// When Manager export all users
		// Then return list size equals 0
		// HttpClientErrorException should occur
		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

	@Test
	public void testPlayerGetAllUsers() {
		// Given server is up & user list size greater than 0
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();

		// When Player export all users
		// Then return list size equals 0
		// HttpClientErrorException should occur
		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

}
