package com.demo.pokerplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.demo.pokerplanning.model.UserStoryModel;

/**
 * 
 * @author ytawri
 *
 */
public interface UserStoryRepository extends JpaRepository<UserStoryModel, String> {
	
	@Query("FROM UserStoryModel u WHERE u.session.sessionId = :session_id and u.id = :id")
	UserStoryModel findUserStoryBySessionIdAndIdNamedParams(
	  @Param("session_id") String sessionId, 
	  @Param("id") String id);
	
	@Query("FROM UserStoryModel u WHERE u.id = :id")
	UserStoryModel getUserStoryById(
	  @Param("id") String id);
}
