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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
public class BindInstanceToInstance {

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
	public void testAdminBindingInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary parentInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary childInstance = this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.ADMIN);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.put(this.url + requestingUser + parentInstance + "/children", childInstance);
		});
	}

	@Test
	public void testManagerBindingInstances() {

		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary parentInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary childInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceIdBoundary childIdBoundary = new InstanceIdBoundary(childInstance.getInstanceId().getDomain(),
				childInstance.getInstanceId().getId());
		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.MANAGER);

		this.client.put(this.url + requestingUser + parentInstance + "/children", childIdBoundary);

		Pageable pageable = PageRequest.of(0, 5, Direction.DESC, "id");
		assertThat(this.testingService.getInstanceDao().findAllByParentsDomainAndParentsId(
				parentInstance.getInstanceId().getDomain(), parentInstance.getInstanceId().getId(), pageable))
						.hasSize(1);
	}

	@Test
	public void testPlayerBindingInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary parentInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary childInstance = this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = this.testingFactory.createNewUser(UserRole.PLAYER);

		assertThrows(HttpClientErrorException.Forbidden.class, () -> {
			this.client.put(this.url + requestingUser + parentInstance + "/children", childInstance);
		});
	}

	@Test
	public void testNonExistingUserBindingInstances() {
		UserBoundary user = this.testingFactory.createNewUser(UserRole.MANAGER);
		InstanceBoundary parentInstance = this.testingFactory.createNewInstance(user.getUserId(), true);
		InstanceBoundary childInstance = this.testingFactory.createNewInstance(user.getUserId(), true);

		UserBoundary requestingUser = new UserBoundary(new UserId("DOMAIN", "EMAIL@MAIL.COM"), UserRole.MANAGER,
				"AVATAR", "USERNAME");

		assertThrows(HttpClientErrorException.NotFound.class, () -> {
			this.client.put(this.url + requestingUser + parentInstance + "/children", childInstance);
		});
	}

}
