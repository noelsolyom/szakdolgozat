package hu.gde.ortem.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.gde.ortem.common.model.ErrorResponse;
import hu.gde.ortem.common.util.OrtemConverterUtil;
import hu.gde.ortem.user.model.User;
import hu.gde.ortem.user.model.UserDto;
import hu.gde.ortem.user.model.UserResponse;
import hu.gde.ortem.user.repository.UserRepository;
import hu.gde.ortem.userrole.model.Role;
import hu.gde.ortem.userrole.model.UserRole;
import hu.gde.ortem.userrole.repository.UserRoleRepository;

/*
* @author  Noel Solyom
*/

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	public UserResponse<UserDto> createUser(UserDto userDto) {

		UserResponse<UserDto> response = new UserResponse<>();
		try {
			LOGGER.info("Creating new user: {}", userDto);

			Optional<User> presentUser = userRepository.findActiveByEmail(userDto.getEmail());

			if (presentUser.isEmpty()) {

				User user = new User();
				user.setName(userDto.getName());
				user.setEmail(userDto.getEmail());
				user.setPassword(OrtemConverterUtil.encodePassword(userDto.getPassword()));

				Optional<UserRole> role = userRoleRepository.findByName(Role.valueOf(userDto.getRole()));

				if (role.isPresent()) {
					user.setRole(role.get());

					user = userRepository.save(user);
					userDto.setId(user.getId());
					userDto.setPassword(null);

					response.setResponse(Optional.of(userDto));

				} else {
					response.setError(Optional.of(new ErrorResponse("Unknown user role.")));
				}
			} else {
				LOGGER.error("Active user is present with the same email address.");
				response.setError(Optional.of(new ErrorResponse("Error creating user: " + userDto.getEmail())));
			}

		} catch (Exception e) {
			LOGGER.error("Error creating user: {}. Cause: {}", userDto, e.getMessage());
			response.setError(Optional.of(new ErrorResponse("Error creating user: " + userDto.getEmail())));
			e.printStackTrace();
		}
		return response;
	}

	public UserResponse<List<UserDto>> getUsers() {
		LOGGER.info("Listing users.");
		Iterable<User> users = userRepository.findAllActive();
		List<User> userList = new ArrayList<>();
		users.forEach(userList::add);

		List<UserDto> userDtoList = OrtemConverterUtil.convertUserListToDtoList(userList);

		UserResponse<List<UserDto>> response = new UserResponse<>();
		response.setResponse(Optional.of(userDtoList));
		return response;
	}

	public UserResponse<UserDto> getUser(String id) {
		LOGGER.info("Getting user by id: {}", id);
		UserResponse<UserDto> response = new UserResponse<>();

		try {
			Long.valueOf(id);
		} catch (Exception e) {
			LOGGER.error("Request paramter is not a number.");
			response.setError(Optional.of(new ErrorResponse("Request parameter is not a number.")));
			return response;
		}

		Optional<User> user = userRepository.findActiveById(Long.valueOf(id));

		UserDto userDto = OrtemConverterUtil.convertUserToDto(user);

		if (userDto != null) {
			response.setResponse(Optional.of(userDto));
		} else {
			response.setError(Optional.of(new ErrorResponse("User not found.")));
		}

		return response;
	}

	public UserResponse<Integer> deleteUser(String id) {
		LOGGER.info("Deleting user by id: {}", id);
		UserResponse<Integer> response = new UserResponse<>();

		try {
			Long.valueOf(id);
		} catch (Exception e) {
			LOGGER.error("Request paramter is not a number.");
			response.setError(Optional.of(new ErrorResponse("Request parameter is not a number.")));
			return response;
		}

		Integer result = userRepository.deleteUser(Long.valueOf(id));

		if (result != null && result.intValue() == 1) {
			response.setResponse(Optional.of(result));
		} else if (result != null && result.intValue() == 0) {
			response.setError(Optional.of(new ErrorResponse("User not found or already deleted.")));
		} else {
			response.setError(Optional.of(new ErrorResponse("Cannot delete user.")));
		}

		return response;
	}

	public UserResponse<UserDto> updateUserPassword(UserDto userDto) {
		LOGGER.info("Updating user's password: {}", userDto);
		UserResponse<UserDto> response = new UserResponse<>();
		try {

			Integer result = userRepository.updateUserPassword(userDto.getId(),
					OrtemConverterUtil.encodePassword(userDto.getPassword()));
			userDto.setPassword(null);
			if (result != null) {
				Optional<User> user = userRepository.findById(userDto.getId());

				if (user.isPresent()) {
					userDto.setName(user.get().getName());
					userDto.setEmail(user.get().getEmail());
					userDto.setId(user.get().getId());
					userDto.setRole(user.get().getRole().getRoleName().toString());
					response.setResponse(Optional.of(userDto));
				} else {
					response.setError(Optional.of(new ErrorResponse("User not found.")));
				}

			} else {
				response.setError(
						Optional.of(new ErrorResponse("Error updating user's password: " + userDto.getEmail())));
			}
		} catch (Exception e) {
			LOGGER.error("Error updating user's password: {}. Cause: {}", userDto, e.getMessage());
			response.setError(Optional.of(new ErrorResponse("Error updating user's password: " + userDto.getEmail())));
		}
		return response;
	}

	public User validate(User user) {
		LOGGER.info("Validating user.");

		try {
			Optional<User> authUser = userRepository.validate(user.getEmail(), user.getPassword());
			if (authUser.isPresent()) {
				LOGGER.info("Validating user complete.");
				return authUser.get();
			} else {
				LOGGER.info("Validating user failed. User not found, deleted or not enabled. {}", user.getEmail());
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Validating user failed. {}, {}", user.getEmail(), e.getMessage());
			return null;
		}
	}

}
