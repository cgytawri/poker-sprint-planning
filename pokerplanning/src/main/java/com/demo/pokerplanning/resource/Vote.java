package com.demo.pokerplanning.resource;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
	
	@NotBlank(message = "MemberId cannot be null")
	@JsonProperty("memberName")
	private String memberName;
	
	@NotBlank(message = "userStoryId cannot be null")
	@JsonProperty("userStoryId")
	private String userStoryId;

	@NotBlank(message = "Vote value cannot be null")
	@JsonProperty("value")
	private String value;

}
