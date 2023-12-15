package hu.gde.ortem.auth.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import hu.gde.ortem.session.model.Session;
import hu.gde.ortem.session.service.SessionService;
import hu.gde.ortem.user.model.User;
import hu.gde.ortem.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
* @author  Noel Solyom
*/

@Service
public class AuthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SessionService sessionService;

	public User validate(User user) {
		LOGGER.info("Trying to validate user.");

		try {
			User validUser = userService.validate(user);
			if (validUser != null) {
				LOGGER.info("Validating user complete.");
				return validUser;
			} else {
				LOGGER.info("Validating user failed.");
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("Validating user failed. {}", e.getMessage());
			return null;
		}
	}

	public void login(User user, HttpServletResponse response) {
		LOGGER.info("Logging in user.");

		try {
			LOGGER.info("Creating session for user.");
			Session session = new Session();
			session.setUser(user);
			session.setSid(UUID.randomUUID().toString());

			LOGGER.info("Saving session for user.");
			sessionService.saveSession(session);

			LOGGER.info("Creating cookie for user.");
			Cookie cookie = new Cookie("SID", session.getSid());
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			response.addCookie(cookie);
			LOGGER.info("Creating cookie for user complete.");
			LOGGER.info("Trying to set lastLogin.");
		} catch (Exception e) {
			LOGGER.error("Logging in user failed. {}", e.getMessage());
		}
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("SID")) {
					LOGGER.info("SID found. Setting sessionId from cookie.");
					String sid = cookie.getValue();
					LOGGER.info("Trying to delete sessionId.");
					sessionService.deleteSessionBySid(sid);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Logging out user failed. {}", e.getMessage());
		}

		LOGGER.info("Creating expired cookie.");
		Cookie cookie = new Cookie("SID", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public boolean checkSession(HttpServletRequest request) {
		LOGGER.info("Checking session.");
		try {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("SID")) {
					LOGGER.info("SID found. Setting sessionId from cookie.");
					String sid = cookie.getValue();
					LOGGER.info("Trying to check sessionId. {}", sid);
					Session session = sessionService.getSessionBySid(sid);
					if (session != null) {
						return true;
					} else {
						return false;
					}
				}
			}
			LOGGER.info("SID not found in cookie.");
			return false;
		} catch (Exception e) {
			LOGGER.error("Checking session failed. {}", e.getMessage());
			return false;
		}
	}

	public String getLoggedInUserSid() {
		LOGGER.info("Getting logged-in user.");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		if (request.getCookies() != null) {
			LOGGER.info("Cookies found.");
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("SID")) {
					LOGGER.info("SID found. Setting sessionId from cookie.");
					return cookie.getValue();
				}
			}
		}
		LOGGER.warn("SID not found.");
		return null;

	}

}
