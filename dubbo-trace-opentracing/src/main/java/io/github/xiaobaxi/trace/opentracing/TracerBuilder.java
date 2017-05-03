package com.hikvision.ga.trace.opentracing;

import com.alibaba.dubbo.common.extension.SPI;

import io.opentracing.Tracer;

/**
 * build the tracer
 * @author fangzhibin
 *
 */
@SPI("zipkin")
public interface TracerBuilder {

	Tracer build(String serverName);

}
