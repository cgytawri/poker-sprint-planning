package com.demo.pokerplanning.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.pokerplanning.model.SessionModel;
import com.demo.pokerplanning.repository.SessionRepository;
import com.demo.pokerplanning.resource.Session;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);
	
	@Value("#{'${poker.planning.deck.types}'.split('[,]')}")
	private List<String> deckTypes;
	
	private final SessionRepository sessionRepository;
	
	@Autowired
	private final MessageSource messageSource;
	
	public Session getSessionById(String sessionId) {
		SessionModel sessionModel = getSession(sessionId);
		return new Session(sessionModel.getSessionId(),sessionModel.getTitle(),sessionModel.getDeckType());
	}
	
	public Session createSession(Session session) {
		if (!deckTypes.contains(session.getDeckType())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					messageSource.getMessage("error.unsupported.deck.type",null,Locale.ENGLISH));
		}
		SessionModel sessionModel = new SessionModel(null, session.getTitle(), session.getDeckType(), null, null);
		sessionModel = sessionRepository.saveAndFlush(sessionModel);
		session.setSessionId(sessionModel.getSessionId());
		return session;
	}

	public List<Session> getSessions() {
		return Optional.ofNullable(sessionRepository.findAll())
			      .map(Collection::stream)
			      .orElseGet(Stream::empty).map(sessionModel -> {
	    	return new Session(sessionModel.getSessionId(),sessionModel.getTitle(),sessionModel.getDeckType());
	    }).collect(Collectors.toList());
	}

	public Session deleteSession(String idSession) {
		Session session = null;
		try {
			SessionModel sessionModel = sessionRepository.getById(idSession);
			session = new Session(sessionModel.getSessionId(),sessionModel.getTitle(),sessionModel.getDeckType());
			sessionRepository.delete(sessionModel);
		}catch(EntityNotFoundException e) {
			LOGGER.error("Session could not be deleted as it was not found");
    		throw new ResponseStatusException(
   		           HttpStatus.NOT_FOUND, messageSource.getMessage("error.session.not.found", new Object[] {idSession},Locale.ENGLISH), e) ;
     	}	
		return session;
	}
	
	protected SessionModel getSession(String idSession) {
		Optional<SessionModel> sessionModel = sessionRepository.findById(idSession);
		sessionModel.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, messageSource.getMessage("error.session.not.found", new Object[] {idSession},Locale.ENGLISH)));
		return sessionModel.get();
	}
}
