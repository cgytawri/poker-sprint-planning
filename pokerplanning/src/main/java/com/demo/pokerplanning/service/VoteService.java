package com.demo.pokerplanning.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import com.demo.pokerplanning.model.MemberModel;
import com.demo.pokerplanning.model.SessionModel;
import com.demo.pokerplanning.model.UserStoryModel;
import com.demo.pokerplanning.model.VoteModel;
import com.demo.pokerplanning.repository.VoteRepository;
import com.demo.pokerplanning.resource.UserStory;
import com.demo.pokerplanning.resource.Vote;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {
	
	private static final String HIDDEN_VALUE = "*";

	private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);
	
	private final VoteRepository voteRepository;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private UserStoryService userStoryService;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private final MessageSource messageSource;

	public Vote emitVote(String idSession, Vote vote) {
		sessionService.getSession(idSession);
		MemberModel memberModel = memberService.getMemberBySessionId(idSession, vote.getMemberName());
		UserStoryModel userStortModel = userStoryService.getUserStory(idSession, vote.getUserStoryId());
		if(!UserStory.StatusEnum.VOTING.getValue().equals(userStortModel.getStatus())) {
			LOGGER.error("Vote cannot be accepted as user story is not in VOTING status");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,messageSource.getMessage("error.voting.not.allowed",null,Locale.ENGLISH));
		}
		VoteModel voteModel = voteRepository.getVoteByUserStoryIdAndMemberId(vote.getUserStoryId(),memberModel.getMemberId());
		if(ObjectUtils.isEmpty(voteModel)) {
			voteModel = new VoteModel();
			voteModel.setUserStory(userStortModel);
			voteModel.setMember(memberModel);
		}else {
			LOGGER.debug("Vote is already emmitted for this story by the member. Overriding the old value");
		}
		voteModel.setVoteValue(vote.getValue());
		voteRepository.saveAndFlush(voteModel);
		vote.setValue(HIDDEN_VALUE);
		return vote;
	}
	
	public List<Vote> getVotesBySessionId(String idSession) {
		SessionModel session = sessionService.getSession(idSession);
		List<Vote> votes = new ArrayList<Vote>();
		List<UserStoryModel> stories = session.getUserStories();
		if(null != stories) {
			for(UserStoryModel userStory: stories) {
				List<VoteModel> voteModels = userStory.getVotes();
				if(null != voteModels) {
					for(VoteModel voteModel : voteModels) {
						Vote vote = new Vote();
						vote.setMemberName(voteModel.getMember().getName());
						vote.setUserStoryId(voteModel.getUserStory().getId());
						if (UserStory.StatusEnum.VOTED.getValue().equals(userStory.getStatus())) {
							vote.setValue(voteModel.getVoteValue());
						} else {
							vote.setValue(HIDDEN_VALUE);
						}
						votes.add(vote);
					}
				}
			}
		}
		return votes;
	}

	public List<Vote> getVotesByStoryId(String idSession, String storyId) {
		List<Vote> votes = null;
		sessionService.getSession(idSession);
		UserStoryModel userStory = userStoryService.getUserStory(idSession, storyId);
		List<VoteModel> voteModels = userStory.getVotes();
		if (null != voteModels) {
			votes = new ArrayList<Vote>();
			for (VoteModel voteModel : voteModels) {
				Vote vote = new Vote();
				vote.setMemberName(voteModel.getMember().getName());
				vote.setUserStoryId(voteModel.getUserStory().getId());
				if (UserStory.StatusEnum.VOTED.getValue().equals(userStory.getStatus())) {
					vote.setValue(voteModel.getVoteValue());
				} else {
					vote.setValue(HIDDEN_VALUE);
				}
				votes.add(vote);
			}
		}
		return votes;
	}
	
}
