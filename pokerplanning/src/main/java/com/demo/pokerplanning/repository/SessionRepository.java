package com.demo.pokerplanning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.pokerplanning.model.SessionModel;

/**
 * 
 * @author ytawri
 *
 */
public interface SessionRepository extends JpaRepository<SessionModel, String> {

}
