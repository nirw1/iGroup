package iob.daos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import iob.attributes.InstanceId;
import iob.data.InstanceEntity;

public interface InstanceDao extends PagingAndSortingRepository<InstanceEntity, InstanceId>{
	
	public List<InstanceEntity> findAllByParentsDomainAndParentsId(String parentDomain, String parentId, Pageable pageable);

	public List<InstanceEntity> findAllByChildrenDomainAndChildrenId(String childDomain, String childId, Pageable pageable);
	
}
