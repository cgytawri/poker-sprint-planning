package com.demo.pokerplanning.resource;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStory {

	@NotBlank(message = "userStoryId cannot be blank")
	@JsonProperty("userStoryId")
	private String userStoryId;

	@JsonProperty("description")
	private String description;

	@JsonProperty("status")
	private StatusEnum status = StatusEnum.PENDING;

	public enum StatusEnum {
		PENDING("PENDING"),

		VOTING("VOTING"),

		VOTED("VOTED");

		private String value;

		StatusEnum(String value) {
			this.value = value;
		}

		@JsonValue
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@JsonCreator
		public static StatusEnum fromValue(String value) {
			for (StatusEnum b : StatusEnum.values()) {
				if (b.value.equals(value)) {
					return b;
				}
			}
			throw new IllegalArgumentException("Unexpected value '" + value + "'");
		}
	}

}
