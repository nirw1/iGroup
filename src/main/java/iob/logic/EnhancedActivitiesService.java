package iob.logic;

import java.util.List;

import iob.boundaries.ActivityBoundary;

public interface EnhancedActivitiesService extends ActivitiesService {

	List<ActivityBoundary> getAllActivities(String userDomain, String userEmail, int page, int size);

}
