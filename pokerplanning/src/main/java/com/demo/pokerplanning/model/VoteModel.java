package com.demo.pokerplanning.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author ytawri
 *
 */
@Entity
@Table(name = "vote")
@Builder(builderMethodName = "vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteModel {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "vote_id")
	private String voteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "story_id", nullable = false)
	private UserStoryModel userStory;

	/*
	 * @Size(max = 50)
	 * 
	 * @Column(name = "session_id") private String sessionId;
	 */

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private MemberModel member;

	@Size(max = 20)
	@Column(name = "vote_value")
	private String voteValue;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name="session_id") private SessionModel session;
	 */

}
