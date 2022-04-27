package com.demo.pokerplanning.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.pokerplanning.resource.Vote;
import com.demo.pokerplanning.service.VoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
@WebMvcTest(VoteController.class)
public class VoteControllerTest {

	@Autowired
    MockMvc mockMvc;
	
	@Autowired
    ObjectMapper mapper;
	
	@MockBean
	private VoteService voteService;
	
	@Test
	public void emitVote_success() throws Exception {
		String sessionId = "78875";
		String userStoryId = "userStory1";
		String memberId = "member1";
		String voteValue = "2";
		Vote vote = new Vote(memberId,userStoryId,voteValue);
	    Mockito.when(voteService.emitVote(sessionId, vote)).thenReturn(vote);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions/"+sessionId+"/votes")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(vote));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.userStoryId", is(userStoryId)))
	            .andExpect(jsonPath("$.memberId", is(memberId)))
	            .andExpect(jsonPath("$.value", is(voteValue)));
	}
	
	@Test
	public void getVotes_success() throws Exception {
		String sessionId = "78875";
		String userStoryId1 = "userStory1";
		String memberId1 = "memberId1";
		String memberId2 = "memberId2";
		String voteValue1 = "2";
		String voteValue2 = "3";
		Vote vote1 = new Vote(memberId1,userStoryId1,voteValue1);
		Vote vote2 = new Vote(memberId2,userStoryId1,voteValue2);
		List<Vote> listOfVotes = Arrays.asList(vote1,vote2);
	    Mockito.when(voteService.getVotesByStoryId(sessionId,userStoryId1)).thenReturn(listOfVotes);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sessions/"+sessionId+"/stories/"+userStoryId1+"/votes")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.[0].userStoryId", is(userStoryId1)))
	            .andExpect(jsonPath("$.[1].userStoryId", is(userStoryId1)))
	            .andExpect(jsonPath("$.[0].memberId", is(memberId1)))
	            .andExpect(jsonPath("$.[1].memberId", is(memberId2)))
	            .andExpect(jsonPath("$.[0].value", is(voteValue1)))
	            .andExpect(jsonPath("$.[1].value", is(voteValue2)));
	}
}
