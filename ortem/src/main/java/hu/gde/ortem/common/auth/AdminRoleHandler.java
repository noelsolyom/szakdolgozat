package hu.gde.ortem.common.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import hu.gde.ortem.auth.service.AuthService;
import hu.gde.ortem.session.model.Session;
import hu.gde.ortem.session.service.SessionService;
import hu.gde.ortem.user.model.User;
import hu.gde.ortem.user.model.UserDto;
import hu.gde.ortem.userrole.model.Role;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Order(30)
@Component
public class AdminRoleHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoleHandler.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	private AuthService authService;

	@Around("@annotation(hu.gde.ortem.common.auth.AdminRole)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();

		String sid = authService.getLoggedInUserSid();
		Session session = null;

		if (sid != null) {
			session = sessionService.getSessionBySid(sid);
		}

		if (session != null) {

			User user = session.getUser();
			if (!user.getRole().getRoleName().equals(Role.ADMIN)) {
				ResponseEntity<UserDto> result = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
				response.sendError(HttpStatus.UNAUTHORIZED.value());
				return result;
			}
		} else {
			LOGGER.info("Session not found.");
			ResponseEntity<UserDto> result = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			response.sendError(HttpStatus.UNAUTHORIZED.value());
			return result;

		}

		Object result = joinPoint.proceed();

		return result;
	}
}