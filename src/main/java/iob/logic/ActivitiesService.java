package iob.logic;

import java.util.List;

import iob.boundaries.ActivityBoundary;

public interface ActivitiesService {
	
	public Object invokeActivity(ActivityBoundary activity);
	public List<ActivityBoundary> gatAllActivities(String adminDomain, String adminEmail);
	public void deleteAllActivities(String adminDomain, String adminEmail);
}
