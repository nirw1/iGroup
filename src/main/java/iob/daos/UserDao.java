package iob.daos;

import org.springframework.data.repository.CrudRepository;

import iob.attributes.UserId;
import iob.data.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, UserId>{

}
