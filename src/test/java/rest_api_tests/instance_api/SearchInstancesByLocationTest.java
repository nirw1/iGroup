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
import org.springframework.web.client.RestTemplate;

import iob.Application;
import iob.attributes.Location;
import iob.attributes.UserId;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;
import iob.logic.TestingDaoService;
import iob.logic.TestingFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class SearchInstancesByLocationTest {

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
		this.url = "http://localhost:" + this.port + "/iob/instances/";
	}

	@AfterEach
	public void after() {
		this.testingService.getInstanceDao().deleteAll();
		this.testingService.getUserDao().deleteAll();
	}

	@Test
	public void testAdminSearchByLocation() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary activeInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary inActiveInstance = this.testingFactory.createNewInstance(user.getUserId(), false);

		Location activeLocation = activeInstance.getLocation();
		Location inActiveLocation = inActiveInstance.getLocation();

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + requestingUser + "/search/near/" + activeLocation + "50",
					InstanceBoundary[].class);
		});

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.getForObject(this.url + requestingUser + "/search/near/" + inActiveLocation + "50",
					InstanceBoundary[].class);
		});
	}

	@Test
	public void testManagerSearchByLocation() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary activeInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		this.testingFactory.createNewInstance(user.getUserId(), false);

		Location activeLocation = activeInstance.getLocation();

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		assertThat(this.client.getForObject(this.url + requestingUser + "/search/near/" + activeLocation + "50",
				InstanceBoundary[].class)).hasSize(2);

		assertThat(this.client.getForObject(this.url + requestingUser + "/search/near/" + activeLocation + "0",
				InstanceBoundary[].class)).hasSize(1);
	}

	@Test
	public void testPlayerSearchByLocation() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary activeInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary inActiveInstance = this.testingFactory.createNewInstance(user.getUserId(), false);

		Location activeLocation = activeInstance.getLocation();
		Location inActiveLocation = inActiveInstance.getLocation();

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		assertThat(this.client.getForObject(this.url + requestingUser + "/search/near/" + activeLocation + "50",
				InstanceBoundary[].class)).hasSize(1);

		assertThat(this.client.getForObject(this.url + requestingUser + "/search/near/" + inActiveLocation + "0",
				InstanceBoundary[].class)).hasSize(0);
	}

	@Test
	public void testNonExistingUserSearchByLocation() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary activeInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		this.testingFactory.createNewInstance(user.getUserId(), false);

		Location activeLocation = activeInstance.getLocation();

		UserBoundary requestingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER,
				"AVATAR", "USERNAME");

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.getForObject(this.url + requestingUser + "/search/near/" + activeLocation + "50",
					InstanceBoundary[].class);
		});

	}

}
