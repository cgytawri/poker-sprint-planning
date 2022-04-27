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

import com.demo.pokerplanning.resource.UserStory;
import com.demo.pokerplanning.service.UserStoryService;

import com.fasterxml.jackson.databind.ObjectMapper;
@WebMvcTest(UserStoryController.class)
public class UserStoryControllerTest {

	@Autowired
    MockMvc mockMvc;
	
	@Autowired
    ObjectMapper mapper;
	
	@MockBean
	private UserStoryService userStoryService;
	
	@Test
	public void createUserStory_success() throws Exception {
		String sessionId = "78875";
		String userStoryId = "userStory1";
		UserStory userStory = new UserStory(userStoryId,"User Story 1",null);
	    Mockito.when(userStoryService.createUserStory(sessionId, userStory)).thenReturn(userStory);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions/"+sessionId+"/stories")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(userStory));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.userStoryId", is(userStoryId)));
	}
	
	@Test
	public void getUserStories_success() throws Exception {
		String sessionId = "78875";
		String userStoryId1 = "userStory1";
		String userStoryId2 = "userStory2";
		UserStory userStory1 = new UserStory(userStoryId1,"User Story 1",null);
		UserStory userStory2 = new UserStory(userStoryId2,"User Story 2",null);
		List<UserStory> listOfStories = Arrays.asList(userStory1,userStory2);
	    Mockito.when(userStoryService.getStoriesBySessionId(sessionId)).thenReturn(listOfStories);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sessions/"+sessionId+"/stories")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.[0]userStoryId", is(userStoryId1)))
	            .andExpect(jsonPath("$.[1]userStoryId", is(userStoryId2)));
	}
	
	@Test
	public void deleteUserStory_success() throws Exception {
		String sessionId = "78875";
		String userStoryId = "userStory1";
		UserStory userStory = new UserStory(userStoryId,"User Story 1",null);
	    Mockito.when(userStoryService.deleteUserStory(sessionId, userStoryId)).thenReturn(userStory);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/sessions/"+sessionId+"/stories/"+userStoryId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(userStory));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.userStoryId", is(userStoryId)));
	}
	
	@Test
	public void updateUserStory_success() throws Exception {
		String sessionId = "78875";
		String userStoryId = "userStory1";
		UserStory userStory = new UserStory(userStoryId,"User Story 1",null);
	    Mockito.when(userStoryService.updateUserStory(sessionId, userStoryId,userStory)).thenReturn(userStory);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/sessions/"+sessionId+"/stories/"+userStoryId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(userStory));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.userStoryId", is(userStoryId)));
	}
	
}
