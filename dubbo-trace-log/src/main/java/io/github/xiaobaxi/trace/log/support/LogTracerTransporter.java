package io.github.xiaobaxi.trace.log.support;

import io.github.xiaobaxi.trace.core.TracerClient;
import io.github.xiaobaxi.trace.core.TracerTransporter;

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

}
