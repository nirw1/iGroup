package admin_role_tests;

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
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.InstanceEntity;
import iob.data.UserRole;
import iob.logic.TestingDaoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class AdminInstancesTests {

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
		this.url = "http://localhost:" + this.port + "/iob/admin/instances/";
		this.createUserUrl = "http://localhost:" + this.port + "/iob/users/";
		this.client = new RestTemplate();
	}

	@BeforeEach
	public void createInstance() {
		InstanceEntity entity = new InstanceEntity();
		entity.setId("1");
		entity.setDomain("DOMAIN");
		this.testingService.getInstanceDao().save(entity);
	}

//
	@AfterEach
	public void deleteAllInstancesAndUsers() {
		this.testingService.getUserDao().deleteAll();
		this.testingService.getInstanceDao().deleteAll();
	}

	@Test
	public void testAdminDeleteAllInstances() {
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();

		this.client.delete(this.url + this.user);
		assertThat(this.testingService.getInstanceDao().findAll()).hasSize(0);
	}

	@Test
	public void testManagerDeleteAllInstances() {
		this.user = this.client.postForObject(this.createUserUrl,
				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
				.getUserId();

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}

	@Test
	public void testPlayerDeleteAllInstances() {
		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.delete(this.url + this.user);
		});
	}
}
