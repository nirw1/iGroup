//package integrative;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import javax.annotation.PostConstruct;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.web.client.RestTemplate;
//
//import iob.Application;
//import iob.attributes.UserId;
//import iob.boundaries.NewUserBoundary;
//import iob.boundaries.UserBoundary;
//import iob.data.UserRole;
//
////@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
//public class AdminUsersTests {
//
//	private String createUserUrl;
//
//	private UserId newAdmin;
//	private UserId admin;
//	private UserId manager;
//	private UserId player;
//
//	private int port;
//	private RestTemplate client;
//	private String url;
//
//	@LocalServerPort
//	public void setPort(int port) {
//		this.port = port;
//	}
//
//	@PostConstruct
//	public void initTestCase() {
//		this.url = "http://localhost:" + this.port + "/iob/admin/users/";
//		this.createUserUrl = "http://localhost:" + this.port + "/iob/users";
//		this.client = new RestTemplate();
////		this.admin = this.client
////				.postForObject(this.createUserUrl,
////						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
////				.getUserId();
////
////		this.manager = this.client.postForObject(this.createUserUrl,
////				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
////				.getUserId();
////
////		this.player = this.client
////				.postForObject(this.createUserUrl,
////						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
////				.getUserId();
//
//	}
//
//	@BeforeEach
//	public void createUsers() {
//		this.admin = this.client
//				.postForObject(this.createUserUrl,
//						new NewUserBoundary("admin@mail.com", UserRole.ADMIN, "admin", "admin"), UserBoundary.class)
//				.getUserId();
//
//		this.manager = this.client.postForObject(this.createUserUrl,
//				new NewUserBoundary("manager@mail.com", UserRole.MANAGER, "manager", "manager"), UserBoundary.class)
//				.getUserId();
//
//		this.player = this.client
//				.postForObject(this.createUserUrl,
//						new NewUserBoundary("player@mail.com", UserRole.PLAYER, "player", "player"), UserBoundary.class)
//				.getUserId();
//	}
//
////
//	@AfterEach
//	public void deleteAllUsers() {
//		this.newAdmin = this.client.postForObject(this.createUserUrl, new NewUserBoundary("after_each_admin@mail.com",
//				UserRole.ADMIN, "after_each_admin", "after_each_admin"), UserBoundary.class).getUserId();
//		// Creating new admin in case the original one was deleted
//
//		this.client.delete(this.url + this.newAdmin);
//	}
//
//	@Test
//	public void testAdminDeleteAllUsers() {
//		// Given server is up & user list size greater than 0
//		// When Admin delete all users
//		this.client.delete(this.url + this.admin);
//
//		// Then users list size equals 0
//		this.newAdmin = this.client.postForObject(this.createUserUrl,
//				new NewUserBoundary("new_admin@mail.com", UserRole.ADMIN, "new_admin", "new_admin"), UserBoundary.class)
//				.getUserId(); // Creating new admin in case the original one was deleted
//
//		// Size must be 1 because we created a new admin user
//		assertThat(this.client.getForObject(this.url + this.newAdmin, UserBoundary[].class)).hasSize(1);
//	}
//
//	@Test
//	public void testManagerDeleteAllUsers() {
//		// Given server is up & user list size greater than 0
//		// When Manager delete all users
//		this.client.delete(this.url + this.manager);
//
//		// Then users list size larger than 0
//		this.newAdmin = this.client.postForObject(this.createUserUrl,
//				new NewUserBoundary("new_admin@mail.com", UserRole.ADMIN, "new_admin", "new_admin"), UserBoundary.class)
//				.getUserId(); // Creating new admin in case the original one was deleted
//
//		// Size must be greater than 1 because we created a new admin user
//		assertThat(this.client.getForObject(this.url + this.newAdmin, UserBoundary[].class)).hasSizeGreaterThan(1);
//	}
//
//	@Test
//	public void testPlayerDeleteAllUsers() {
//		// Given server is up & user list size greater than 0
//		// When Player delete all users
//		this.client.delete(this.url + this.player);
//
//		// Then users list size larger than 0
//		this.newAdmin = this.client.postForObject(this.createUserUrl,
//				new NewUserBoundary("new_admin@mail.com", UserRole.ADMIN, "new_admin", "new_admin"), UserBoundary.class)
//				.getUserId(); // Creating new admin in case the original one was deleted
//
//		// Size must be greater than 1 because we created a new admin user
//		assertThat(this.client.getForObject(this.url + this.newAdmin, UserBoundary[].class)).hasSizeGreaterThan(1);
//	}
//
//	@Test
//	public void testAdminGetAllUsers() {
//		// Given server is up & user list size greater than 0
//		// When Admin export all users
//		// Then return list size is greater than 0
//
//		assertThat(this.client.getForObject(this.url + this.admin, UserBoundary[].class)).hasSizeGreaterThan(0);
//	}
//
//	@Test
//	public void testManagerGetAllUsers() {
//		// Given server is up & user list size greater than 0
//		// When Manager export all users
//		// Then return list size equals 0
//
//		assertThat(this.client.getForObject(this.url + this.manager, UserBoundary[].class)).hasSize(0);
//	}
//
//	@Test
//	public void testPlayerGetAllUsers() {
//		// Given server is up & user list size greater than 0
//		// When Player export all users
//		// Then return list size equals 0
//
//		assertThat(this.client.getForObject(this.url + this.player, UserBoundary[].class)).hasSize(0);
//	}
//
//}
