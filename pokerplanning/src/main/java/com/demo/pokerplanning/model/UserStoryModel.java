/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
@Table(name = "userstory",indexes = @Index(name = "idx_userstory_sid_id", columnList = "id,session_id", unique = true))
@Builder(builderMethodName = "userstory" )
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryModel {

	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "story_id")
    private String storyId;
	
	@Size(max = 20)
	@Column(name = "id")
    private String id;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @Size(max = 10)
    @Column(name = "status")
    private String status;
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id",nullable=false)
	private SessionModel session;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VoteModel> votes;

}
