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

import iob.Application;
import iob.attributes.UserId;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.TestingDaoService;
import iob.logic.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class CreateNewInstanceTest {

	@Autowired
	private TestingDaoService testingService;

	@Autowired
	private TestingFactory testingFactory;

	private int port;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void postConstruct() {
		this.testingFactory.setPort(this.port);
	}

	@AfterEach
	public void after() {
		this.testingService.getInstanceDao().deleteAll();
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testCreateAdmin() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.ADMIN);
		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.testingFactory.createNewInstance(user.getUserId(), true);
		});
	}

	@Test
	public void testCreateManager() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		this.testingFactory.createNewInstance(user.getUserId(), true);
		assertThat(this.testingService.getInstanceDao().findAll()).hasSize(1);
	}

	@Test
	public void testCreatePlayer() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.PLAYER);
		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.testingFactory.createNewInstance(user.getUserId(), true);
		});
	}

	@Test
	public void testNonExistingUser() {
		UserBoundary user = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER, "AVATAR",
				"USERNAME");
		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.testingFactory.createNewInstance(user.getUserId(), true);
		});
	}
}
