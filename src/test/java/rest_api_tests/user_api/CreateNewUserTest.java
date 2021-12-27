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

import iob.Application;
import iob.data.UserRole;
import iob.tests_helpers.TestingDaoService;
import iob.tests_helpers.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class CreateNewUserTest {

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
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testCreateAdmin() {
		this.testingFactory.createNewUser(UserRole.ADMIN);
		assertThat(this.testingService.getUserDao().findAll()).hasSize(1);
	}

	@Test
	public void testCreateManager() {
		this.testingFactory.createNewUser(UserRole.MANAGER);
		assertThat(this.testingService.getUserDao().findAll()).hasSize(1);
	}

	@Test
	public void testCreatePlayer() {
		this.testingFactory.createNewUser(UserRole.PLAYER);
		assertThat(this.testingService.getUserDao().findAll()).hasSize(1);
	}
}
