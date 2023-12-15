package hu.gde.ortem.common.auth;

import java.time.LocalDateTime;

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
import hu.gde.ortem.user.model.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Aspect
@Order(20)
@Component
public class AuthenticationHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	private AuthService authService;

	@Around("@annotation(hu.gde.ortem.common.auth.Authenticated)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();

		String sid = authService.getLoggedInUserSid();
		Session session = null;

		if (sid != null) {
			session = sessionService.getSessionBySid(sid);
		}

		if (sid == null || session == null || session.getCreationDate().isAfter(LocalDateTime.now().plusHours(24L))) {
			LOGGER.error("Session not found or expired! Sid: {}", sid);
			LOGGER.info("Creating expired cookie.");
			Cookie cookie = new Cookie("SID", null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);

			ResponseEntity<UserDto> result = new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
			response.sendError(HttpStatus.UNAUTHORIZED.value());
			return result;
		} else {
			session.setCreationDate(LocalDateTime.now());
			sessionService.prolongSession(session);
		}

		Object result = joinPoint.proceed();

		return result;
	}

}