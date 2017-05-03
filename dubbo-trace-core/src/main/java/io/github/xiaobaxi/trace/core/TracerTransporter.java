package io.github.xiaobaxi.trace.core;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * 
 * @author fangzhibin
 *
 */
@SPI("simple")
public interface TracerTransporter {
	
	public TracerClient createTc();
	
}
