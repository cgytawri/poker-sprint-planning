package com.demo.pokerplanning;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.Random;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import com.demo.pokerplanning.config.ApplicationConfig;
import com.demo.pokerplanning.resource.Member;
import com.demo.pokerplanning.resource.Session;
import com.demo.pokerplanning.resource.UserStory;
import com.demo.pokerplanning.resource.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { ApplicationConfig.class, PokerplanningApplication.class })
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.properties")
@ActiveProfiles("integration-test")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class PokerPlanningApplicationIT {

	private MockMvc mockMvc;

	@MockBean
	private Tracer tracer;

	@MockBean
	private Span span;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private String sessionId;

	private String memberId;

	private String storyId;

	@BeforeEach
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		Mockito.doReturn(span).when(tracer).nextSpan();
	}

	@Test
	@Order(1)
	public void integrationTestSetup() {
		ServletContext servletContext = webApplicationContext.getServletContext();
		Assert.notNull(servletContext, "servletContext cannot be null");
		Assert.isTrue(servletContext instanceof MockServletContext,
				"servletContext must be an object of MockServletContext");
		Assert.notNull(webApplicationContext.getBean("sessionController"), "sessionController cannot be null");
	}

	@Test
	@Order(2)
	public void testCreateSessionWithValidInputs() throws Exception {
		Session input = new Session(null, generateRandomName(), "Fibonacci");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(input));
		MvcResult result = this.mockMvc.perform(mockRequest).andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.sessionId", notNullValue()))
				.andExpect(jsonPath("$._links.self.href", notNullValue())).andReturn();
		Session session = mapper.readValue(result.getResponse().getContentAsString(), Session.class);
		sessionId = session.getSessionId();
	}

	@Test
	@Order(3)
	public void testAddMemberToSessionWithValidInputs() throws Exception {
		Member memberInput = new Member(null, generateRandomName());
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions/" + sessionId + "/members")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(memberInput));
		MvcResult result = mockMvc.perform(mockRequest).andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.memberId", notNullValue())).andReturn();
		Member member = mapper.readValue(result.getResponse().getContentAsString(), Member.class);
		memberId = member.getMemberId();
	}

	@Test
	@Order(4)
	public void testAddUserStoryToSessionWithValidInputs() throws Exception {
		UserStory userStory = new UserStory(generateRandomName(), "User Story 1", null);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions/" + sessionId + "/stories")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(userStory));
		MvcResult result = mockMvc.perform(mockRequest).andExpect(status().isCreated())
				.andExpect(jsonPath("$", notNullValue())).andExpect(jsonPath("$.userStoryId", notNullValue()))
				.andExpect(jsonPath("$.status", is(UserStory.StatusEnum.PENDING.toString()))).andReturn();
		UserStory story = mapper.readValue(result.getResponse().getContentAsString(), UserStory.class);
		storyId = story.getUserStoryId();
	}

	@Test
	@Order(5)
	public void testUpdateUserStoryWithValidInputs() throws Exception {
		UserStory userStory = new UserStory(storyId, "test story desc", UserStory.StatusEnum.VOTING);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.put("/sessions/" + sessionId + "/stories/" + storyId).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(this.mapper.writeValueAsString(userStory));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.userStoryId", is(storyId)))
				.andExpect(jsonPath("$.status", is(UserStory.StatusEnum.VOTING.toString())));
	}

	@Test
	@Order(6)
	public void testEmitVoteWithValidInputs() throws Exception {
		String voteValue = "5";
		Vote vote = new Vote(memberId, storyId, voteValue);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sessions/" + sessionId + "/votes")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(vote));
		mockMvc.perform(mockRequest).andExpect(status().isCreated()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.userStoryId", is(storyId))).andExpect(jsonPath("$.memberId", is(memberId)))
				.andExpect(jsonPath("$.value", is("*")));
	}

	@Test
	@Order(7)
	public void testdeleteSessionWithValidInputs() throws Exception {
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/sessions/" + sessionId)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.sessionId", is(sessionId)));
	}

	public String generateRandomName() {
		int n = 10;
		byte[] array = new byte[256];
		new Random().nextBytes(array);
		String randomString = new String(array, Charset.forName("UTF-8"));
		StringBuffer r = new StringBuffer();
		for (int k = 0; k < randomString.length(); k++) {
			char ch = randomString.charAt(k);
			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) && (n > 0)) {
				r.append(ch);
				n--;
			}
		}
		return r.toString();
	}
}
