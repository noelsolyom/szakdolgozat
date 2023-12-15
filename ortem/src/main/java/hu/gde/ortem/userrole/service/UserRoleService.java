package hu.gde.ortem.userrole.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.gde.ortem.common.model.ErrorResponse;
import hu.gde.ortem.common.util.OrtemConverterUtil;
import hu.gde.ortem.userrole.model.Role;
import hu.gde.ortem.userrole.model.UserRole;
import hu.gde.ortem.userrole.model.UserRoleDto;
import hu.gde.ortem.userrole.model.UserRoleResponse;
import hu.gde.ortem.userrole.repository.UserRoleRepository;

/*
* @author  Noel Solyom
*/

@Service
public class UserRoleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRoleService.class);

	@Autowired
	private UserRoleRepository userRoleRepository;

	public UserRoleResponse<UserRoleDto> createUserRole(UserRoleDto userRoleDto) {

		UserRoleResponse<UserRoleDto> response = new UserRoleResponse<>();
		try {
			LOGGER.info("Creating new user role: {}", userRoleDto.getRoleName());

			UserRole userRole = new UserRole();
			userRole.setRoleName(Role.valueOf(userRoleDto.getRoleName()));

			userRoleRepository.save(userRole);
			response.setResponse(Optional.of(userRoleDto));
		} catch (Exception e) {
			LOGGER.error("Error creating user role: {}. Cause: {}", userRoleDto.getRoleName(), e.getMessage());
			response.setError(Optional.of(new ErrorResponse("Error creating user role: " + userRoleDto.getRoleName())));
		}
		return response;
	}

	public UserRoleResponse<List<UserRoleDto>> getUserRoles() {
		Iterable<UserRole> userRoles = userRoleRepository.findAllActive();
		List<UserRole> userRoleList = new ArrayList<>();
		userRoles.forEach(userRoleList::add);

		List<UserRoleDto> userRoleDtoList = OrtemConverterUtil.convertUserRoleListToDtoList(userRoleList);

		UserRoleResponse<List<UserRoleDto>> response = new UserRoleResponse<>();
		response.setResponse(Optional.of(userRoleDtoList));
		return response;
	}

}
