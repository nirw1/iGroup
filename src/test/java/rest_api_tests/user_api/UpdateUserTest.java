package rest_api_tests.user_api;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import iob.Application;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.TestingDaoService;
import iob.logic.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class UpdateUserTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private UserBoundary user;

	private RestTemplate client;
	private String url;
	private int port;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void postConstruct() {
		this.url = "http://localhost:" + this.port + "/iob/users/";
		this.testingFactory.setPort(this.port);
		this.client = new RestTemplate();
	}

	@AfterEach
	public void after() {
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testUpdateUser() {

		this.user = this.testingFactory.createNewUser(UserRole.ADMIN);
		UserBoundary userNew = this.testingFactory.createNewUser(UserRole.MANAGER);
		UserBoundary userOld = this.client.getForObject(this.url + "login/" + this.user, UserBoundary.class);

		this.client.put(this.url + this.user, userNew);

		userNew = this.client.getForObject(this.url + "login/" + this.user, UserBoundary.class);

		assertThat(userNew.getAvatar().equals(userOld.getAvatar())).isFalse();
		assertThat(userNew.getRole().equals(userOld.getRole())).isFalse();
		assertThat(userNew.getUsername().equals(userOld.getUsername())).isFalse();

		assertThat(userNew.getUserId().equals(userOld.getUserId())).isTrue();

	}
}
