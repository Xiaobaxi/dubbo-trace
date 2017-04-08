package com.hikvision.ga.trace.log.support;

import com.hikvision.ga.trace.core.TracerClient;
import com.hikvision.ga.trace.core.TracerServletClient;
import com.hikvision.ga.trace.core.TracerTransporter;

/**
 * 
 * @author fangzhibin
 *
 */
public class LogTracerTransporter implements TracerTransporter {

	@Override
	public TracerClient createTc() {
		return new LogTracerClient();
	}

	@Override
	public TracerServletClient createTsc() {
		return null;
	}

}
