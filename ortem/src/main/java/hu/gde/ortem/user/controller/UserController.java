package hu.gde.ortem.user.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.gde.ortem.common.auth.AdminRole;
import hu.gde.ortem.common.auth.Authenticated;
import hu.gde.ortem.user.model.UserDto;
import hu.gde.ortem.user.model.UserResponse;
import hu.gde.ortem.user.service.UserService;

/*
* @author  Noel Solyom
*/

@RestController
@RequestMapping(value = "/api/admin")
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Authenticated
	@AdminRole
	@PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserResponse<UserDto>> createUser(@RequestBody UserDto userDto) {
		LOGGER.info("Create new user request arrived: {}", userDto);
		UserResponse<UserDto> response = userService.createUser(userDto);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@GetMapping(value = "/users", produces = "application/json")
	public ResponseEntity<UserResponse<List<UserDto>>> getUsers() {
		LOGGER.info("List users request arrived.");
		UserResponse<List<UserDto>> response = userService.getUsers();

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@GetMapping(value = "/user", produces = "application/json")
	public ResponseEntity<UserResponse<UserDto>> getUser(@RequestParam String id) {
		LOGGER.info("Get user by id request arrived.");
		UserResponse<UserDto> response = userService.getUser(id);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@AdminRole
	@DeleteMapping(value = "/user", produces = "application/json")
	public ResponseEntity<UserResponse<Integer>> deleteUser(@RequestParam String id) {
		LOGGER.info("Delete user by id request arrived.");
		UserResponse<Integer> response = userService.deleteUser(id);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@AdminRole
	@PatchMapping(value = "/user/password", produces = "application/json")
	public ResponseEntity<UserResponse<UserDto>> updateUserPassword(@RequestBody UserDto userDto) {
		LOGGER.info("Update user password request arrived.");
		UserResponse<UserDto> response = userService.updateUserPassword(userDto);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

}
