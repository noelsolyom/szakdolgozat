package hu.gde.ortem.session.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import hu.gde.ortem.session.model.Session;
import hu.gde.ortem.user.model.User;

/*
* @author  Noel Solyom
*/

public interface SessionRepository extends CrudRepository<Session, Long> {

	@Query("SELECT s FROM Session s WHERE s.sid = :sid AND s.deleted = false")
	public Optional<Session> getSessionBySid(@Param("sid") String sid);

	@Query("SELECT s.user FROM Session s WHERE s.sid = :sid AND s.deleted = false")
	public Optional<User> getUserBySid(@Param("sid") String sid);
}