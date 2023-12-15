package hu.gde.ortem.session.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.gde.ortem.session.model.Session;
import hu.gde.ortem.session.repository.SessionRepository;
import hu.gde.ortem.user.model.User;

/*
* @author  Noel Solyom
*/

@Service
public class SessionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

	@Autowired
	SessionRepository sessionRepository;

	public User getLoggedInUser(String sid) {
		LOGGER.info("Getting logged in user.");

		try {
			Optional<User> user = sessionRepository.getUserBySid(sid);
			if (user.isPresent()) {
				LOGGER.info("User found.");
				return user.get();
			}
			LOGGER.info("User not found.");
			return null;
		} catch (Exception e) {
			LOGGER.error("Getting user by session failed. {}", e.getMessage());
			return null;
		}
	}

	public void saveSession(Session session) {
		LOGGER.info("Saving session.");
		try {
			sessionRepository.save(session);
			LOGGER.info("Saving session complete.");
		} catch (Exception e) {
			LOGGER.error("Saving session failed. {}, {}", session, e.getMessage());
		}

	}

	public void deleteSessionBySid(String sid) {
		LOGGER.info("Deleting sid.");

		try {
			Optional<Session> session = sessionRepository.getSessionBySid(sid);
			if (session.isPresent()) {
				LOGGER.info("Sid found. Deleting.");
				sessionRepository.delete(session.get());
				LOGGER.info("Deleting session cpomolete.");
			} else {
				LOGGER.info("Sid not found. Nothing to delete.");
			}
		} catch (Exception e) {
			LOGGER.error("Deleting session failed. {}", e.getMessage());
		}
	}

	public Session getSessionBySid(String sid) {
		LOGGER.info("Gettinget session by sessionId. {}", sid);
		try {
			Optional<Session> session = sessionRepository.getSessionBySid(sid);
			if (session.isPresent()) {
				LOGGER.info("Session found.");
				return session.get();
			}
			LOGGER.info("Session not found.");
			return null;
		} catch (Exception e) {
			LOGGER.error("Getting session by sid failed. {}", e.getMessage());
			return null;
		}
	}

	public void prolongSession(Session session) {
		LOGGER.info("Prolonging session.");
		try {
			sessionRepository.save(session);
			LOGGER.info("Prolonging session complete.");
		} catch (Exception e) {
			LOGGER.error("Prolonging session failed. {}", e.getMessage());
		}
	}

}
