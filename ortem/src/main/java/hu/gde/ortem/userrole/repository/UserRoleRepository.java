package hu.gde.ortem.userrole.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import hu.gde.ortem.userrole.model.Role;
import hu.gde.ortem.userrole.model.UserRole;

/*
* @author  Noel Solyom
*/

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {

	@Query("SELECT ur FROM UserRole ur WHERE ur.deleted = false ORDER BY ur.id")
	public Iterable<UserRole> findAllActive();

	@Query("SELECT ur FROM UserRole ur WHERE ur.deleted = false AND ur.roleName = :name ORDER BY ur.id")
	public Optional<UserRole> findByName(@Param("name") Role name);
}
