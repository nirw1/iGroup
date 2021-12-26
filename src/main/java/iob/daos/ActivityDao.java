package iob.daos;

import org.springframework.data.repository.PagingAndSortingRepository;

import iob.attributes.ActivityId;
import iob.data.ActivityEntity;

public interface ActivityDao extends PagingAndSortingRepository<ActivityEntity, ActivityId> {

}
