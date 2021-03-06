package com.demo.pokerplanning.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.demo.pokerplanning.model.SessionModel;
import com.demo.pokerplanning.model.UserStoryModel;
import com.demo.pokerplanning.repository.UserStoryRepository;
import com.demo.pokerplanning.resource.UserStory;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserStoryService.class);
	
	private final UserStoryRepository userStoryRepository;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private final MessageSource messageSource;

	public UserStory createUserStory(String idSession, UserStory userStory) {
		SessionModel session = sessionService.getSession(idSession);
		try {
			UserStoryModel usModel = new UserStoryModel();	
			usModel.setSession(session);
			usModel.setDescription(userStory.getDescription());
			usModel.setId(userStory.getUserStoryId());
			usModel.setStatus(UserStory.StatusEnum.PENDING.getValue());
			usModel = userStoryRepository.saveAndFlush(usModel);
			userStory.setStatus(UserStory.StatusEnum.fromValue(usModel.getStatus()));
		}catch(DataIntegrityViolationException e) {
			LOGGER.error("DataIntegrityViolationException occurred while creating user story");
			throw new ResponseStatusException(
	     		           HttpStatus.BAD_REQUEST, messageSource.getMessage("error.record.already.exists",null,Locale.ENGLISH));			
		}
		return userStory;
	}

	public UserStory deleteUserStory(String idSession, String idUserStory) {
		UserStory userStory = null;
		UserStoryModel model = userStoryRepository.findUserStoryBySessionIdAndIdNamedParams(idSession,idUserStory);
		if(null == model) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,messageSource.getMessage("error.user.story.not.found", new Object[] {idUserStory,idSession},Locale.ENGLISH));
		}else if(!UserStory.StatusEnum.PENDING.getValue().equals(model.getStatus())){
			LOGGER.error("User story cannot be deleted as it is not in pending state");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,messageSource.getMessage("error.invalid.user.story.status",null,Locale.ENGLISH));
		}else {
			userStory = new UserStory(model.getId(),model.getDescription(),UserStory.StatusEnum.fromValue(model.getStatus()));
			userStoryRepository.delete(model);
		}
		return userStory;
	}

	public UserStory updateUserStory(String idSession, String idUserStory, UserStory userStory) {
		UserStoryModel model = getUserStory(idSession, idUserStory);
		if (StringUtils.isNotEmpty(userStory.getDescription())) {
			model.setDescription(userStory.getDescription());
		}else {
			userStory.setDescription(model.getDescription());
		}
		if (null != userStory.getStatus()) {
			model.setStatus(userStory.getStatus().getValue());
		}
		userStoryRepository.saveAndFlush(model);
		userStory.setUserStoryId(model.getId());
		return userStory;
	}
	
	public List<UserStory> getStoriesBySessionId(String idSession) {
		SessionModel session = sessionService.getSession(idSession);
		return Optional.ofNullable(session.getUserStories()).map(Collection::stream)
				.orElseGet(Stream::empty).map(userStoryModel -> {
					UserStory story = new UserStory();
					story.setDescription(userStoryModel.getDescription());
					story.setUserStoryId(userStoryModel.getId());
					story.setStatus(UserStory.StatusEnum.fromValue(userStoryModel.getStatus()));
					return story;
				}).collect(Collectors.toList());
	}
	
	protected UserStoryModel getUserStory(String idSession, String idUserStory) {
		UserStoryModel model = userStoryRepository.findUserStoryBySessionIdAndIdNamedParams(idSession,idUserStory);
		if(null == model) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,messageSource.getMessage("error.user.story.not.found", new Object[] {idUserStory,idSession},Locale.ENGLISH));
		}
		return model;
	}
}
