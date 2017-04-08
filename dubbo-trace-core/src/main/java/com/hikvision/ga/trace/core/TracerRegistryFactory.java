package com.hikvision.ga.trace.core;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.ConfigUtils;

/**
 * 
 * @author fangzhibin
 *
 */
public class TracerRegistryFactory {
	
	private final TracerClient tc;
	
	private final TracerServletClient tsc;
	
	public TracerRegistryFactory() {
		String type = ConfigUtils.getProperty(TracerConstants.TRACE_TYPE, "simple");
		TracerTransporter tt = ExtensionLoader.getExtensionLoader(TracerTransporter.class).getExtension(type);
		tc = tt.createTc();
		
		tsc = tt.createTsc();
	}
	
	public TracerClient getTracerClient() {
		return tc;
	}
	
	public TracerServletClient getTracerServletClient() {
		return tsc;
	}

}
