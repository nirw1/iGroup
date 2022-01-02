package rest_api_tests.user_api;

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
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.tests_helpers.TestingDaoService;
import iob.tests_helpers.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("testing")
public class LoginUserTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private UserId user;

	private RestTemplate client;
	private String url;
	private int port;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void postConstruct() {
		this.url = "http://localhost:" + this.port + "/iob/users/login/";
		this.testingFactory.setPort(this.port);
		this.client = new RestTemplate();
	}

	@AfterEach
	public void after() {
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testLoginAdmin() {
		this.user = testingFactory.createNewUser(UserRole.ADMIN).getUserId();
		assertThat(this.client.getForObject(this.url + this.user, UserBoundary.class)).isNotNull()
				.hasSameClassAs(new UserBoundary());
	}

	@Test
	public void testLoginManager() {
		this.user = this.testingFactory.createNewUser(UserRole.MANAGER).getUserId();
		assertThat(this.client.getForObject(this.url + this.user, UserBoundary.class)).isNotNull()
				.hasSameClassAs(new UserBoundary());
	}

	@Test
	public void testLoginPlayer() {
		this.user = this.testingFactory.createNewUser(UserRole.PLAYER).getUserId();
		assertThat(this.client.getForObject(this.url + this.user, UserBoundary.class)).isNotNull()
				.hasSameClassAs(new UserBoundary());
	}

	@Test
	public void testInvalidUserLogin() {
		UserId invalidUser = new UserId("DOMAIN", "NOTEXIST@EMAIL.COM");
		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.getForObject(this.url + invalidUser, UserBoundary.class);
		});
	}
}
