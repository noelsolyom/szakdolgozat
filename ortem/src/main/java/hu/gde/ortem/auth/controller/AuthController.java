package hu.gde.ortem.auth.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.gde.ortem.auth.model.AuthUserDto;
import hu.gde.ortem.auth.service.AuthService;
import hu.gde.ortem.common.util.OrtemConverterUtil;
import hu.gde.ortem.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
* @author  Noel Solyom
*/

@RestController
@RequestMapping(value = "/api/admin")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
	public AuthUserDto login(@RequestBody AuthUserDto authUserDto, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LOGGER.info("Trying to log in user.");

		try {

			User user = authService.validate(OrtemConverterUtil.convertAuthUserDtoToUser(authUserDto));

			if (user != null) {

				authService.login(user, response);

				LOGGER.info("Logging in user complete.");
			} else {
				response.sendError(HttpStatus.UNAUTHORIZED.value());
				LOGGER.info("Logging in user failed.");
			}

		} catch (Exception e) {
			LOGGER.error("Logging in user failed. {}", e.getMessage());

			response.sendError(HttpStatus.UNAUTHORIZED.value());
		}
		authUserDto.setPassword(null);
		return authUserDto;
	}

	@GetMapping(value = "/logout", produces = "application/json")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOGGER.info("Trying to log out user.");

		try {
			authService.logout(request, response);
			LOGGER.info("SID ok.");
			return ResponseEntity.ok().build();

		} catch (Exception e) {
			LOGGER.error("Logout failed. {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	}

	@GetMapping(value = "/checkSession", produces = "application/json")
	public ResponseEntity<Void> checkSession(HttpServletRequest request) throws IOException {
		LOGGER.info("Trying to check session.");

		try {
			if (authService.checkSession(request)) {
				LOGGER.info("SID ok.");
				return ResponseEntity.ok().build();
			} else {
				LOGGER.info("SID not ok.");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} catch (Exception e) {
			LOGGER.error("Checking session failed. {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

}
