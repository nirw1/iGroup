package player_role_tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

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
import iob.boundaries.InstanceBoundary;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.InstanceEntity;
import iob.data.UserRole;
import iob.logic.TestingDaoService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("Testing")
public class PlayerInstancesTest {

	@Autowired
	private TestingDaoService testingService;

	private InstanceEntity activeInstance;
	private InstanceEntity inactiveInstance;

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
		this.url = "http://localhost:" + this.port + "/";
		this.createUserUrl = "http://localhost:" + this.port + "/iob/users/";
		this.client = new RestTemplate();

		this.user = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();
	}

	@BeforeEach
	public void createInstances() {
		this.activeInstance = new InstanceEntity();
		this.activeInstance.setId("1");
		this.activeInstance.setDomain("DOMAIN");
		this.activeInstance.setActive(true);
		this.activeInstance.setName("NAME");
		this.activeInstance.setType("TYPE");
		this.activeInstance.setLongitude(this.port);
		this.activeInstance.setLatitude(this.port);
		this.activeInstance.setCreatedTimestamp(new Date(this.port));

		this.inactiveInstance = new InstanceEntity();
		this.inactiveInstance.setId("2");
		this.inactiveInstance.setDomain("DOMAIN");
		this.inactiveInstance.setActive(false);
		this.inactiveInstance.setName("NAME");
		this.inactiveInstance.setType("TYPE");
		this.inactiveInstance.setLongitude(this.port);
		this.inactiveInstance.setLatitude(this.port);
		this.inactiveInstance.setCreatedTimestamp(new Date(this.port));

		this.testingService.getInstanceDao().save(this.activeInstance);
		this.testingService.getInstanceDao().save(this.inactiveInstance);
	}

	@AfterEach
	public void deleteAllInstancesAndUsers() {
		this.testingService.getInstanceDao().deleteAll();
	}

	@Test
	public void testPlayerRetriveInactiveInstance() {
		// Given there are both active & inactive instances in DB
		// When player search for instances by name
		// Then only active instances will returned
		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.getForObject(this.url + "/iob/instances/" + this.user + "/" + this.inactiveInstance.getDomain()
					+ "/" + this.inactiveInstance.getId(), InstanceBoundary.class);
		});
	}

//	@Test
//	public void testPlayerSearchInstancesByName() {
//		// Given there are both active & inactive instances in DB
//		// When player search for instances by name
//		// Then only active instances will returned
//		assertThat(this.client.getForObject(this.url + this.user + "/search/byName/NAME", InstanceBoundary[].class))
//				.hasSize(1).allMatch((instance) -> instance.getActive());
//	}
}
