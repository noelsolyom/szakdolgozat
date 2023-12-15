package hu.gde.ortem.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import hu.gde.ortem.common.util.OrtemConverterUtil;
import hu.gde.ortem.user.model.User;
import hu.gde.ortem.user.model.UserDto;
import hu.gde.ortem.userrole.model.Role;
import hu.gde.ortem.userrole.model.UserRole;

@SpringBootTest
public class TestUserUtils {

	@Test
	public void testConvertUserToUserDto() {
		User user = new User();
		user.setName("John Doe");
		user.setEmail("john_doe@mail.com");
		user.setPassword("dummy_password");
		user.setId(1L);
		user.setDeleted(Boolean.FALSE);
		user.setCreationDate(LocalDateTime.now());
		UserRole role = new UserRole();
		role.setRoleName(Role.ADMIN);
		user.setRole(role);

		UserDto userDto = OrtemConverterUtil.convertUserToDto(Optional.of(user));

		assertEquals("John Doe", userDto.getName());
		assertNull(userDto.getPassword());
		assertEquals(Role.ADMIN.toString(), userDto.getRole());

	}

	@Test
	public void testConvertUserToUserDtoEmptyUser() {

		UserDto userDto = OrtemConverterUtil.convertUserToDto(Optional.empty());

		assertNull(userDto);

	}

	@Test
	public void testEncodePassword() {

		String regExp = "^[a-f0-9]{64}";
		Pattern pattern = Pattern.compile(regExp);

		String dummyPassword = "dummy_password";

		String encoded = OrtemConverterUtil.encodePassword(dummyPassword);

		Matcher matcher = pattern.matcher(encoded);

		assertTrue(matcher.find());
	}

}
