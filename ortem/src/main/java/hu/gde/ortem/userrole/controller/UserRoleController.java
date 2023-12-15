package hu.gde.ortem.userrole.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gde.ortem.common.auth.AdminRole;
import hu.gde.ortem.common.auth.Authenticated;
import hu.gde.ortem.userrole.model.UserRoleDto;
import hu.gde.ortem.userrole.model.UserRoleResponse;
import hu.gde.ortem.userrole.service.UserRoleService;

/*
* @author  Noel Solyom
*/

@RestController
@RequestMapping(value = "/api/admin")
public class UserRoleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleController.class);

	@Autowired
	private UserRoleService userRoleService;

	@Authenticated
	@AdminRole
	@PostMapping(value = "/user-role", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserRoleResponse<UserRoleDto>> createUserRole(@RequestBody UserRoleDto userRoleDto) {
		LOGGER.info("Create new user role request arrived: {}", userRoleDto);
		UserRoleResponse<UserRoleDto> response = userRoleService.createUserRole(userRoleDto);

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Authenticated
	@GetMapping(value = "/user-role", produces = "application/json")
	public ResponseEntity<UserRoleResponse<List<UserRoleDto>>> getUserRoles() {
		LOGGER.info("List user roles request arrived.");
		UserRoleResponse<List<UserRoleDto>> response = userRoleService.getUserRoles();

		if (response.getResponse().isPresent()) {
			return ResponseEntity.ok().body(response);
		} else if (response.getError().isPresent()) {
			return ResponseEntity.internalServerError().body(response);
		} else {
			return ResponseEntity.internalServerError().build();
		}
	}

}
