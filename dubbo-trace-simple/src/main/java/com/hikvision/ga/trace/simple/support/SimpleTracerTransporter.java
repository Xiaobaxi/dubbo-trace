package com.hikvision.ga.trace.simple.support;

import com.hikvision.ga.trace.core.TracerClient;
import com.hikvision.ga.trace.core.TracerServletClient;
import com.hikvision.ga.trace.core.TracerTransporter;

/**
 * 
 * @author fangzhibin
 *
 */
public class SimpleTracerTransporter implements TracerTransporter {

	@Override
	public TracerClient createTc() {
		return new SimpleTracerClient();
	}

	@Override
	public TracerServletClient createTsc() {
		return new SimpleServletClient();
	}

}
