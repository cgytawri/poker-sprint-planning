package com.demo.pokerplanning.resource;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

	@JsonProperty("sessionId")
	private String sessionId;

	@NotBlank(message = "session title cannot be blank")
	@JsonProperty("title")
	private String title;

	@JsonProperty("deckType")
	private String deckType;

}
