package io.github.xiaobaxi.trace.simple.support;

import io.github.xiaobaxi.trace.core.TracerClient;
import io.github.xiaobaxi.trace.core.TracerTransporter;

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

}
