package integrative;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import iob.Application;
import iob.attributes.ActivityId;
import iob.attributes.CreatedBy;
import iob.attributes.Instance;
import iob.attributes.InstanceId;
import iob.attributes.Location;
import iob.attributes.UserId;
import iob.boundaries.ActivityBoundary;
import iob.boundaries.InstanceBoundary;
import iob.boundaries.NewUserBoundary;
import iob.boundaries.UserBoundary;
import iob.data.UserRole;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
public class AdminActivitiesTests {

	private String createInstanceUrl;
	private InstanceBoundary instance;

	private String createActivityUrl;
	private ActivityBoundary activity;

	private String createUserUrl;
	private UserId admin;
	private UserId manager;
	private UserId player;

	private String url;
	private int port;
	private RestTemplate client;

	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void postConstruct() {
		this.url = "http://localhost:" + this.port + "/iob/admin/activities/";
		this.createUserUrl = "http://localhost:" + this.port + "/iob/users/";
		this.createActivityUrl = "http://localhost:" + this.port + "/iob/activities/";
		this.createInstanceUrl = "http://localhost:" + this.port + "/iob/instances/";
		this.client = new RestTemplate();

		this.admin = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();

		this.manager = this.client.postForObject(this.createUserUrl,
				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
				.getUserId();

		this.player = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
				.getUserId();

		this.instance = new InstanceBoundary(null, "Test", "Test", true, null, new CreatedBy(this.admin), null, null);
		this.instance = this.client.postForObject(this.createInstanceUrl + this.admin, this.instance,
				InstanceBoundary.class);

	}

	@BeforeEach
	public void createActivity() {
		this.admin = this.client
				.postForObject(this.createUserUrl,
						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
				.getUserId();
		this.activity = new ActivityBoundary(null, new Instance(this.instance.getInstanceId()), "Test", null,
				new CreatedBy(this.admin), null);
		this.activity = this.client.postForObject(this.createActivityUrl, this.activity, ActivityBoundary.class);
	}

//
	@AfterEach
	public void deleteAllActivities() {
		this.client.delete(this.url + this.admin);
	}

	@Test
	@Transactional
	public void testAdminDeleteAllActivities() {
		this.client.delete(this.url + this.admin);
		assertThat(this.client.getForObject(this.url + this.admin, ActivityBoundary[].class)).hasSize(0);
	}
	
	@Test
	@Transactional
	public void testManagerDeleteAllActivities() {
		this.client.delete(this.url + this.manager);
		assertThat(this.client.getForObject(this.url + this.admin, ActivityBoundary[].class)).hasSizeGreaterThan(0);
	}
//	
//	@Test
//	public void testPlayerDeleteAllActivities() {
//		this.client.delete(this.url + this.player);
//		assertThat(this.client.getForObject(this.url + this.admin, ActivityBoundary[].class)).hasSizeGreaterThan(0);
//	}
}
