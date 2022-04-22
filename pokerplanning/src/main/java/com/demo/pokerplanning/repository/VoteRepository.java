package com.demo.pokerplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.pokerplanning.model.VoteModel;

/**
 * 
 * @author ytawri
 *
 */
public interface VoteRepository extends JpaRepository<VoteModel, String> {

	@Query("FROM VoteModel u WHERE u.userStory.id = :story_id and u.member.memberId = :member_id")
	VoteModel getVoteByUserStoryIdAndMemberId(@Param("story_id") String storyId, @Param("member_id") String memberId);

}
