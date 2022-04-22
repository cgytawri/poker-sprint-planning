package com.demo.pokerplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.pokerplanning.model.MemberModel;

/**
 * 
 * @author ytawri
 *
 */
public interface MemberRepository extends JpaRepository<MemberModel, String> {
	
	@Query("FROM MemberModel u WHERE u.session.sessionId = :session_id and u.memberId = :member_id")
	MemberModel getMembersBySessionId(
	  @Param("session_id") String sessionId, @Param("member_id") String memberId);


}
