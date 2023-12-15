package hu.gde.ortem.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hu.gde.ortem.user.model.User;
import hu.gde.ortem.user.repository.UserRepository;
import hu.gde.ortem.userrole.model.Role;
import hu.gde.ortem.userrole.model.UserRole;
import hu.gde.ortem.userrole.repository.UserRoleRepository;

@SpringBootTest
public class TestUserRepository {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Test
	public void testGetUsers() {
		Iterable<User> users = userRepository.findAllActive();

		assertTrue(users.iterator().hasNext());
	}

	@Test
	public void testGetUser() {
		Iterable<User> users = userRepository.findAllActive();

		Iterator<User> iter = users.iterator();

		if (iter.hasNext()) {
			User user = iter.next();

			assertEquals(user.getEmail(), userRepository.findById(user.getId()).get().getEmail());
		}
	}

	@Test
	public void testSaveUser() {

		User user = new User();
		user.setEmail("test@test.com");
		user.setName("Test User");
		user.setPassword("passw0rd");

		Optional<UserRole> userRole = userRoleRepository.findByName(Role.ADMIN);
		user.setRole(userRole.get());

		User newUser = userRepository.save(user);

		assertNotNull(newUser);

		Integer i = userRepository.deleteUser(newUser.getId());

		assertEquals(1, i);

	}

	@Test
	public void testUpdateUser() {

		User user = new User();
		user.setEmail("test@test.com");
		user.setName("Test User");
		user.setPassword("passw0rd");

		Optional<UserRole> userRole = userRoleRepository.findByName(Role.ADMIN);
		user.setRole(userRole.get());

		User newUser = userRepository.save(user);

		assertNotNull(newUser);

		userRepository.updateUserPassword(newUser.getId(), "newPassw0rd");

		assertEquals(userRepository.findById(user.getId()).get().getPassword(), "newPassw0rd");

		userRepository.deleteUser(newUser.getId());

	}

}
