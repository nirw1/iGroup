package rest_api_tests_admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import iob.boundaries.InstanceIdBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.TestingDaoService;
import iob.logic.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class DeleteAllInstancesTest {

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
		this.url = "http://localhost:" + this.port + "/iob/admin/instances/";
	}

	@AfterEach
	public void after() {
		this.testingService.getInstanceDao().deleteAll();
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminDeleteAllInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		assertDoesNotThrow(() -> {
			this.client.delete(this.url + requestingUser);
		});
		assertThat(this.testingService.getInstanceDao().findAll()).hasSize(0);
	}

	@Test
	public void testManagerDeleteAllInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + requestingUser);
		});
	}

	@Test
	public void testPlayerDeleteAllInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + requestingUser);
		});
	}

	@Test
	public void testNonExistingUserDeleteAllInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER,
				"AVATAR", "USERNAME");

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.delete(this.url + requestingUser);
		});
	}

}
