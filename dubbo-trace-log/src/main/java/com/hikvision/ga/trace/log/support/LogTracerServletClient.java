package com.hikvision.ga.trace.log.support;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.hikvision.ga.trace.core.TracerServletClient;

public class LogTracerServletClient implements TracerServletClient {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

	}

	@Override
	public void destroy() {

	}

}
