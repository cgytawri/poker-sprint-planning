package com.demo.pokerplanning.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo.pokerplanning.resource.Member;
import com.demo.pokerplanning.resource.Session;
import com.demo.pokerplanning.resource.UserStory;
import com.demo.pokerplanning.resource.Vote;
import com.demo.pokerplanning.service.MemberService;
import com.demo.pokerplanning.service.SessionService;
import com.demo.pokerplanning.service.UserStoryService;
import com.demo.pokerplanning.service.VoteService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author ytawri
 *
 */
@RestController
@RequiredArgsConstructor
public class PokerPlanningController {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserStoryService userStoryService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private VoteService voteService;

	/**
	 * endpoint to create session
	 * 
	 * @param Session
	 * @return created session along with link/URL for created session
	 */
	@PostMapping(value = "/sessions", produces = { "application/json" }, consumes = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public EntityModel<Session> createSession(@Valid @RequestBody(required = true) Session Session) {
		Session = sessionService.createSession(Session);
		EntityModel<Session> model = EntityModel.of(Session);
		WebMvcLinkBuilder linkToSession = linkTo(methodOn(this.getClass()).getSession(Session.getSessionId()));
		model.add(linkToSession.withSelfRel());
		return model;
	}
	
	/**
	 * Endpoint to fetch the list of active sessions
	 * 
	 * @return list of sessions 
	 */

	@GetMapping(value = "/sessions", produces = { "application/json" })
	public ResponseEntity<List<Session>> getSessions() {
		List<Session> sessionList = sessionService.getSessions();
		return new ResponseEntity<List<Session>>(sessionList, HttpStatus.OK);
	}

	/**
	 * Endpoint to fetch session details based on session id
	 * 
	 * @param sessionId
	 * @return session
	 */
	
	@GetMapping(value = "/sessions/{sessionId}", produces = { "application/json" })
	public ResponseEntity<Session> getSession(@PathVariable("sessionId") String sessionId) {
		Session Session = sessionService.getSessionById(sessionId);
		return new ResponseEntity<Session>(Session, HttpStatus.OK);
	}
	
	/**
	 * Endpoint to destroy the session. All the information related to session is deleted as part of this operation
	 * 
	 * @param sessionId
	 * @return session
	 */
	@RequestMapping(value = "/sessions/{sessionId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<Session> deleteSession(@PathVariable("sessionId") String sessionId) {
		Session session = sessionService.deleteSession(sessionId);
		return new ResponseEntity<Session>(session, HttpStatus.OK);
	}

	/**
	 * registers a member to an existing session
	 * 
	 * @param sessionId
	 * @param member
	 * @return member
	 */
	
	@PostMapping(value = "/sessions/{sessionId}/members", produces = { "application/json" }, consumes = {
			"application/json" })
	public ResponseEntity<Member> registerMemberToSession(@PathVariable("sessionId") String sessionId,
			@Valid @RequestBody(required = true) Member member) {
		member = memberService.registerMemberToSession(sessionId, member);
		return new ResponseEntity<Member>(member, HttpStatus.CREATED);
	}
	
	/**
	 * creates a user story
	 * 
	 * @param sessionId
	 * @param userStory
	 * @return user story
	 */
	
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

	/**
	 * Endpoint to return registered members for a session
	 * 
	 * @param sessionId
	 * @return list of members
	 */
	
	@GetMapping(value = "/sessions/{sessionId}/members", produces = { "application/json" })
	public ResponseEntity<List<Member>> getRegisteredMembersForSession(@PathVariable("sessionId") String sessionId) {
		List<Member> members = memberService.getMembersForSession(sessionId);
		return ResponseEntity.ok(members);
	}

	/**
	 * Unregisters/deletes a member from the session
	 * 
	 * @param sessionId
	 * @param memberId
	 * @return
	 */
	@RequestMapping(value = "/sessions/{sessionId}/members/{memberId}", produces = {
			"application/json" }, method = RequestMethod.DELETE)
	public ResponseEntity<Member> getRegisterdMember(@PathVariable("sessionId") String sessionId,
			@PathVariable("memberId") String memberId) {
		Member members = memberService.deleteMembersFromSession(sessionId, memberId);
		return new ResponseEntity<Member>(members, HttpStatus.OK);
	}

	/**
	 * Endpoint to emit vote
	 * 
	 * @param sessionId
	 * @param vote
	 * @return vote
	 */

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
	ResponseEntity<List<Vote>> userStoryIdVotesGet(@PathVariable("sessionId") String sessionId,
			@PathVariable("storyId") String storyId) {
		sessionService.getSessionById(sessionId);
		List<Vote> votes = voteService.getVotesByStoryId(sessionId, storyId);
		return new ResponseEntity<List<Vote>>(votes, HttpStatus.OK);
	}

}
