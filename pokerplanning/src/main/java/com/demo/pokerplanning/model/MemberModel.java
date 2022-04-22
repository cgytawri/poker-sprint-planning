package com.demo.pokerplanning.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "member",indexes = @Index(name = "idx_member_sid_name", columnList = "name,session_id", unique = true))
@Builder(builderMethodName = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberModel {
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "member_id")
	private String memberId;

	@Size(max = 100)
	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id",nullable=false)
	private SessionModel session;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VoteModel> votes;

}
