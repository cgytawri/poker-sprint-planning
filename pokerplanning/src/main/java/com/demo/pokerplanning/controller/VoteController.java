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
import org.springframework.web.bind.annotation.RestController;

import com.demo.pokerplanning.resource.Vote;
import com.demo.pokerplanning.service.VoteService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author ytawri
 *
 */
@RestController
@RequiredArgsConstructor
public class VoteController {

	@Autowired
	private VoteService voteService;

	@PostMapping(value = "/sessions/{sessionId}/votes", produces = { "application/json" }, consumes = {
			"application/json" })
	public ResponseEntity<Vote> emitVote(@PathVariable("sessionId") String sessionId,
			@Valid @RequestBody(required = true) Vote vote) {
		Vote emittedVote = voteService.emitVote(sessionId, vote);
		return new ResponseEntity<Vote>(emittedVote, HttpStatus.CREATED);
	}

	/**
	 * Endpoint to fetch votes for a given user story
	 * 
	 * @param sessionId
	 * @param storyId
	 * @return list of votes
	 */
	
	@GetMapping(value = "/sessions/{sessionId}/stories/{storyId}/votes", produces = { "application/json" })
	ResponseEntity<List<Vote>> getVotesListForUserStory(@PathVariable("sessionId") String sessionId,
			@PathVariable("storyId") String storyId) {
		List<Vote> votes = voteService.getVotesByStoryId(sessionId, storyId);
		return new ResponseEntity<List<Vote>>(votes, HttpStatus.OK);
	}

}
