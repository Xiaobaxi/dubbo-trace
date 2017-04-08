package com.hikvision.ga.trace.opentracing.zipkin;

import io.opentracing.Tracer;
import io.opentracing.impl.BraveTracer;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.EmptySpanCollectorMetricsHandler;
import com.github.kristofa.brave.http.HttpSpanCollector;
import com.hikvision.ga.trace.core.TracerConstants;
import com.hikvision.ga.trace.opentracing.TracerBuilder;

/**
 * zipkin tracer builder
 * @author fangzhibin
 *
 */
public class ZipkinTraceBuilder implements TracerBuilder {

	@Override
	public Tracer build(String serverName) {
		HttpSpanCollector.Config spanConfig = HttpSpanCollector.Config.builder().compressionEnabled(false).connectTimeout(5000)
				.flushInterval(1).readTimeout(6000).build();

		String server = "";
		String enabled = ConfigUtils.getProperty(TracerConstants.TRACE_ENABLED, Boolean.FALSE.toString());
		if (Boolean.TRUE.toString().equalsIgnoreCase(enabled)) {
			server = ConfigUtils.getProperty(TracerConstants.ZIPLIN_SERVER, "http://127.0.0.1:9411");
		}

		Tracer braveTracer = new BraveTracer((new Brave.Builder(serverName)).spanCollector(HttpSpanCollector.create(server, spanConfig,
				new EmptySpanCollectorMetricsHandler())));

		return braveTracer;
	}

}
