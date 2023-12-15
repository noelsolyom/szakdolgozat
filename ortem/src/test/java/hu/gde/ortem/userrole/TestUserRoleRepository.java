package hu.gde.ortem.userrole;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hu.gde.ortem.userrole.model.Role;
import hu.gde.ortem.userrole.model.UserRole;
import hu.gde.ortem.userrole.repository.UserRoleRepository;

@SpringBootTest
public class TestUserRoleRepository {

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Test
	public void testGetRoles() {
		Iterable<UserRole> roles = userRoleRepository.findAllActive();

		assertTrue(roles.iterator().hasNext());
	}

	@Test
	public void testGetRoleAdmin() {
		Optional<UserRole> role = userRoleRepository.findByName(Role.ADMIN);

		assertTrue(role.isPresent());
	}

}
