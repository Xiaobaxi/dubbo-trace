package io.github.xiaobaxi.trace.opentracing.support;

import io.github.xiaobaxi.trace.core.TracerClient;
import io.github.xiaobaxi.trace.core.TracerTransporter;

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

}
