package iob.daos;

import org.springframework.data.repository.CrudRepository;

import iob.attributes.InstanceId;
import iob.data.InstanceEntity;

public interface InstanceDao extends CrudRepository<InstanceEntity, InstanceId>{

}
