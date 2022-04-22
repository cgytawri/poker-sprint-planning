package com.demo.pokerplanning.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.pokerplanning.resource.UserStory;
import com.demo.pokerplanning.service.UserStoryService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author ytawri
 *
 */
@RestController
@RequiredArgsConstructor
public class UserStoryController {

	@Autowired
	private UserStoryService userStoryService;
	
	@PostMapping(value = "/sessions/{sessionId}/stories", produces = { "application/json" }, consumes = {
			"application/json" })
	public ResponseEntity<UserStory> createUserStory(@PathVariable("sessionId") String sessionId,
			@Valid @RequestBody(required = true) UserStory userStory) {
		userStory = userStoryService.createUserStory(sessionId, userStory);
		return new ResponseEntity<UserStory>(userStory, HttpStatus.CREATED);
	}
	
	/**
	 * returns list of user stories
	 * 
	 * @param sessionId
	 * @return list of stories
	 */
	@GetMapping(value = "/sessions/{sessionId}/stories", produces = { "application/json" })
	public ResponseEntity<List<UserStory>> getUserStories(@PathVariable("sessionId") String sessionId) {
		List<UserStory> listOfStories = userStoryService.getStoriesBySessionId(sessionId);
		return new ResponseEntity<List<UserStory>>(listOfStories, HttpStatus.OK);
	}
	
	/**
	 * updates user story
	 * 
	 * @param sessionId
	 * @param userStoryId
	 * @param userStory
	 * @return userStory
	 */
	@RequestMapping(value = "/sessions/{sessionId}/stories/{userStoryId}", produces = {
			"application/json" }, consumes = { "application/json" }, method = RequestMethod.PUT)
	public ResponseEntity<UserStory> updateUserStory(@PathVariable("sessionId") String sessionId,
			@PathVariable("userStoryId") String userStoryId, @Valid @RequestBody(required = true) UserStory userStory) {
		UserStory story = userStoryService.updateUserStory(sessionId, userStoryId, userStory);
		return new ResponseEntity<UserStory>(story, HttpStatus.OK);
	}

	/**
	 * deletes a user story
	 * 
	 * @param sessionId
	 * @param userStoryId
	 * @return userStory
	 */
	
	@RequestMapping(value = "/sessions/{sessionId}/stories/{userStoryId}", produces = {
			"application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<UserStory> deleteUserStory(@PathVariable("sessionId") String sessionId,
			@PathVariable("userStoryId") String userStoryId) {
		UserStory story = userStoryService.deleteUserStory(sessionId, userStoryId);
		return new ResponseEntity<UserStory>(story, HttpStatus.OK);
	}

}
