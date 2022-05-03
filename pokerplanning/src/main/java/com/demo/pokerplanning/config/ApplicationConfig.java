package com.demo.pokerplanning.config;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ApplicationConfig {
	private static final String TRACE_ID = "trace-id";


	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:/messages/error_messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	
	@Bean
    Filter traceIdInResponseFilter(Tracer tracer) {
        return (request, response, chain) -> {
            Span currentSpan = tracer.currentSpan();
            if (currentSpan != null) {
                HttpServletResponse resp = (HttpServletResponse) response;
                if(null == resp.getHeader(TRACE_ID)){
                	resp.addHeader(TRACE_ID, currentSpan.context().traceId());
                }
            }
            chain.doFilter(request, response);
        };
    }
}
