package com.demo.pokerplanning.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PokerPlanningUtil {
	
	private final MessageSource messageSource;
	
	public String getErrorMessage(String key) {
		return messageSource.getMessage(key, null, Locale.ENGLISH);
	}
}
