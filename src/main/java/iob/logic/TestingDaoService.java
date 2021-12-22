package iob.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import iob.daos.ActivityDao;
import iob.daos.InstanceDao;
import iob.daos.UserDao;

@Component
@Profile("Testing")
public class TestingDaoService {
	private UserDao userDao;
	private ActivityDao activityDao;
	private InstanceDao instanceDao;

	@Autowired
	public TestingDaoService(UserDao userDao, ActivityDao activityDao, InstanceDao instanceDao) {
		this.userDao = userDao;
		this.activityDao = activityDao;
		this.instanceDao = instanceDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public ActivityDao getActivityDao() {
		return activityDao;
	}

	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}

	public InstanceDao getInstanceDao() {
		return instanceDao;
	}

	public void setInstanceDao(InstanceDao instanceDao) {
		this.instanceDao = instanceDao;
	}

}
