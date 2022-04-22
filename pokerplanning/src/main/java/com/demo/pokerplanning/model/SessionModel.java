package com.demo.pokerplanning.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "session")
@Builder(builderMethodName = "session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionModel {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "session_id")
	private String sessionId;

	@Size(max = 1000)
	@Column(name = "title")
	private String title;

	@Column(name = "deck_type")
	private String deckType;

	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MemberModel> members;

	public void addMember(MemberModel member) {
		if (null == members) {
			members = new ArrayList<MemberModel>();
		}
		members.add(member);
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserStoryModel> userStories;

}
