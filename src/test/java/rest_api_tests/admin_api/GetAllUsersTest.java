package rest_api_tests.admin_api;

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
import iob.attributes.UserId;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.tests_helpers.TestingDaoService;
import iob.tests_helpers.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("testing")
public class GetAllUsersTest {

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
		this.url = "http://localhost:" + this.port + "/iob/admin/users/";
	}
	
	@BeforeEach
	public void before() {
		this.testingFactory.createNewUser(UserRole.ADMIN);
		this.testingFactory.createNewUser(UserRole.MANAGER);
		this.testingFactory.createNewUser(UserRole.PLAYER);
	}
	
	@AfterEach
	public void after() {
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminGetAllUsers() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		assertDoesNotThrow(() -> {
			assertThat(this.client.getForObject(this.url + requestingUser, UserBoundary[].class)).hasSize(4);
		});

	}

	@Test
	public void testManagerGetAllUsers() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + requestingUser, UserBoundary[].class);
		});
	}

	@Test
	public void testPlayerGetAllUsers() {
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + requestingUser, UserBoundary[].class);
		});
	}

	@Test
	public void testNonExistingUserGetAllUsers() {
		UserBoundary requestingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER,
				"AVATAR", "USERNAME");

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.getForObject(this.url + requestingUser, UserBoundary[].class);
		});
	}

}
