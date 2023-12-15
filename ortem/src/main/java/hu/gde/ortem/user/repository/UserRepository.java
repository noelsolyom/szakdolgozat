package hu.gde.ortem.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import hu.gde.ortem.user.model.User;
import jakarta.transaction.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.deleted = false ORDER BY u.id")
	public Iterable<User> findAllActive();

	@Query("SELECT u FROM User u WHERE u.deleted = false AND u.id = :id ORDER BY u.id")
	public Optional<User> findActiveById(@Param("id") Long id);

	@Query("SELECT u FROM User u WHERE u.deleted = false AND u.email = :email ORDER BY u.id")
	public Optional<User> findActiveByEmail(@Param("email") String email);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.deleted = true WHERE u.id = :id")
	public Integer deleteUser(@Param("id") Long id);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
	public Integer updateUserPassword(@Param("id") Long id, @Param("password") String password);

	@Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.deleted = false")
	public Optional<User> validate(@Param("email") String email, @Param("password") String password);

}
