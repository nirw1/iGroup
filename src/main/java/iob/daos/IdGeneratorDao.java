package iob.daos;

import org.springframework.data.repository.CrudRepository;
import iob.data.IdGenerator;

public interface IdGeneratorDao extends CrudRepository<IdGenerator, Long>{

}
