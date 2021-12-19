package iob.daos;

import org.springframework.data.repository.CrudRepository;

import iob.attributes.ActivityId;
import iob.data.ActivityEntity;

public interface ActivityDao extends CrudRepository<ActivityEntity, ActivityId> {

}
