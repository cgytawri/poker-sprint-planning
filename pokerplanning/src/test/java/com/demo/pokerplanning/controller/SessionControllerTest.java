package com.demo.pokerplanning.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.demo.pokerplanning.resource.Member;
import com.demo.pokerplanning.resource.Session;
import com.demo.pokerplanning.service.MemberService;
import com.demo.pokerplanning.service.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
@WebMvcTest(SessionController.class)
public class SessionControllerTest {

	@Autowired
    MockMvc mockMvc;
	
	@Autowired
    ObjectMapper mapper;
	
	@MockBean
	private SessionService sessionService;

	@MockBean
	private MemberService memberService;
	
	@MockBean
	private Tracer tracer;
	
	private static final String BASE_URL = "http://localhost/sessions";
	
	@Test
	public void createSession_success() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		Session input = new Session(null,"session1","fibonacci");
		Session response = new Session(sessionId,"session1","fibonacci");	    
	    Mockito.when(sessionService.createSession(input)).thenReturn(response);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(input));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.sessionId", is(sessionId)))
	            .andExpect(jsonPath("$._links.self.href", is(BASE_URL+ "/"+ sessionId)));
	}
	
	@Test
	public void createSession_failure() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		Session input = new Session(null,null,"fibonacci");
		Session response = new Session(sessionId,null,"fibonacci");	    
	    Mockito.when(sessionService.createSession(input)).thenReturn(response);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(input));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isBadRequest());
	}
	
	@Test
	public void getSessions_success() throws Exception {
		String sessionId1 = UUID.randomUUID().toString();
		String sessionId2 = UUID.randomUUID().toString();
		Session session1 = new Session(sessionId1,"session1","fibonacci");
		Session session2 = new Session(sessionId2,"session2","fibonacci");
	    List<Session> listOfSessions = Arrays.asList(session1,session2);		
	    Mockito.when(sessionService.getSessions()).thenReturn(listOfSessions);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sessions")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", Matchers.hasSize(2)))
	            .andExpect(jsonPath("$.[0].sessionId", is(sessionId1)))
	            .andExpect(jsonPath("$.[1].sessionId", is(sessionId2)));
	 }
	
	@Test
	public void getSession_success() throws Exception {
		String sessionId1 = UUID.randomUUID().toString();
		Session session1 = new Session(sessionId1,"session1","fibonacci");		
	    Mockito.when(sessionService.getSessionById(sessionId1)).thenReturn(session1);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sessions/"+sessionId1)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.sessionId", is(sessionId1)));
	 }
	
	@Test
	public void deleteSession_success() throws Exception {
		String sessionId1 = UUID.randomUUID().toString();
		Session session1 = new Session(sessionId1,"session1","fibonacci");		
	    Mockito.when(sessionService.deleteSession(sessionId1)).thenReturn(session1);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/sessions/"+sessionId1)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.sessionId", is(sessionId1)));
	 }
	
	@Test
	public void addMember_success() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		String memberId = UUID.randomUUID().toString();
		Member memberInput = new Member(null,"Member1");	
		Member memberResponse = new Member(memberId,"Member1");
	    Mockito.when(memberService.registerMemberToSession(sessionId,memberInput)).thenReturn(memberResponse);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions/"+sessionId+"/members")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .content(this.mapper.writeValueAsString(memberInput));
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$", notNullValue()))
	            .andExpect(jsonPath("$.memberId", is(memberId)));
	}
	
	@Test
	public void deleteMember_success() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		String memberId = UUID.randomUUID().toString();	
		Member memberResponse = new Member(memberId,"Member1");
	    Mockito.when(memberService.deleteMembersFromSession(sessionId,memberId)).thenReturn(memberResponse);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/sessions/"+sessionId+"/members/"+memberId)
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.memberId", is(memberId)));
	}
	
	@Test
	public void getMembers_success() throws Exception {
		String sessionId = UUID.randomUUID().toString();
		String memberId1 = UUID.randomUUID().toString();	
		Member member1 = new Member(memberId1,"Member1");
		String memberId2 = UUID.randomUUID().toString();	
		Member member2 = new Member(memberId2,"Member2");
		List<Member> listOfMembers = Arrays.asList(member1,member2);		
	    Mockito.when(memberService.getMembersForSession(sessionId)).thenReturn(listOfMembers);	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sessions/"+sessionId+"/members")
	            .contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON);
	    mockMvc.perform(mockRequest)
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$", Matchers.hasSize(2)))
	            .andExpect(jsonPath("$.[0].memberId", is(memberId1)))
	            .andExpect(jsonPath("$.[1].memberId", is(memberId2)));
	 }
}
