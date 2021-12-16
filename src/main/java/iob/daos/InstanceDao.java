package iob.daos;

import org.springframework.data.repository.PagingAndSortingRepository;
import iob.attributes.InstanceId;
import iob.data.InstanceEntity;

public interface InstanceDao extends PagingAndSortingRepository<InstanceEntity, InstanceId>{

}
