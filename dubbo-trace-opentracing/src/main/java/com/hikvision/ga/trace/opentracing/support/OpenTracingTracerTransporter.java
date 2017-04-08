package com.hikvision.ga.trace.opentracing.support;

import com.hikvision.ga.trace.core.TracerClient;
import com.hikvision.ga.trace.core.TracerServletClient;
import com.hikvision.ga.trace.core.TracerTransporter;

/**
 * 
 * @author fangzhibin
 *
 */
public class OpenTracingTracerTransporter implements TracerTransporter {

	@Override
	public TracerClient createTc() {
		return new OpenTracingClient();
	}

	@Override
	public TracerServletClient createTsc() {
		return new OpenTracingServletClient();
	}

}
