package com.demo.pokerplanning.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import com.demo.pokerplanning.model.MemberModel;
import com.demo.pokerplanning.model.SessionModel;
import com.demo.pokerplanning.repository.MemberRepository;
import com.demo.pokerplanning.resource.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);
	
	private final MemberRepository memberRepository;
	
	@Autowired
	private final MessageSource messageSource;

	@Autowired SessionService sessionService;
	
	public Member registerMemberToSession(String idSession, Member member) {
		SessionModel sessionModel = sessionService.getSession(idSession);
		try {
			MemberModel memberModel = new MemberModel(null,member.getName(),sessionModel,null);
			memberRepository.saveAndFlush(memberModel);
			member.setMemberId(memberModel.getMemberId());
		}catch(DataIntegrityViolationException e) {
			LOGGER.error("DataIntegrityViolationException occurred while registering member to session ");
			throw new ResponseStatusException(
	     		           HttpStatus.BAD_REQUEST, messageSource.getMessage("error.record.already.exists",null,Locale.ENGLISH));			
		}
		return member;		
	}

	public List<Member> getMembersForSession(String idSession) {
		SessionModel sessionModel = sessionService.getSession(idSession);
		return Optional.ofNullable(sessionModel.getMembers()).map(Collection::stream)
				.orElseGet(Stream::empty).map(memberModel -> {
					return new Member(memberModel.getMemberId(),memberModel.getName());
				}).collect(Collectors.toList());
	}
	
	public Member deleteMembersFromSession(String idSession, String idMember) {
		MemberModel member = getMemberBySessionId(idSession, idMember);
		memberRepository.delete(member);
		return new Member(member.getMemberId(),member.getName());
	}

	protected MemberModel getMemberBySessionId(String idSession, String idMember) {
		MemberModel member =  memberRepository.getMemberBySessionId(idSession, idMember);
		if(ObjectUtils.isEmpty(member)) {
			String errorMessage = messageSource.getMessage("error.member.not.found", new Object[] {idMember},Locale.ENGLISH);
			LOGGER.error(errorMessage);
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,errorMessage);
		}
		return member;
	}

	
}
