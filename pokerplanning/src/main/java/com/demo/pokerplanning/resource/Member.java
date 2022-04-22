package com.demo.pokerplanning.resource;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

	@JsonProperty("memberId")
	private String memberId;

	@NotBlank(message = "Member name cannot be blank")
	@JsonProperty("name")
	private String name;

}
