package iob.daos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import iob.attributes.InstanceId;
import iob.data.InstanceEntity;

public interface InstanceDao extends PagingAndSortingRepository<InstanceEntity, InstanceId>{
	
	public List<InstanceEntity> findAllByParentsDomainAndParentsId(String parentDomain, String parentId, Pageable pageable);

	public List<InstanceEntity> findAllByChildrenDomainAndChildrenId(String childDomain, String childId, Pageable pageable);
	
	public List<InstanceEntity> findAllByName(String name, Pageable pageable);

	public List<InstanceEntity> findAllByType(String type, Pageable pageable);
	
	public List<InstanceEntity> findAllByLatitudeLessThanEqualAndLatitudeGreaterThanEqualAndLongitudeLessThanEqualAndLongitudeGreaterThanEqual(
			@Param("maxLatitudeInclusive") double maxLatitudeInclusive, 
			@Param("minLatitudeInclusive") double minLatitudeInclusive,
			@Param("maxLongitudeInclusive") double maxLongitudeInclusive, 
			@Param("minLongitudeInclusive") double minLongitudeInclusive,
			Pageable pageable);
	
	public List<InstanceEntity> findAllByCreatedTimestampGreaterThanEqual(Date date, Pageable pageable);
	
	public List<InstanceEntity> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
	
	
}
