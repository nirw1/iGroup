package iob.daos;

import org.springframework.data.repository.PagingAndSortingRepository;

import iob.attributes.UserId;
import iob.data.UserEntity;

public interface UserDao extends PagingAndSortingRepository<UserEntity, UserId>{

}
