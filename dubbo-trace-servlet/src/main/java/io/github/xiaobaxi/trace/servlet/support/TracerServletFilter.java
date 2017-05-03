package io.github.xiaobaxi.trace.servlet.support;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.google.common.base.Stopwatch;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
import io.github.xiaobaxi.trace.core.TracerConstants;
import io.github.xiaobaxi.trace.simple.Ids;
import io.github.xiaobaxi.trace.simple.ServerInfo;
import io.github.xiaobaxi.trace.simple.TraceAgent;
import io.github.xiaobaxi.trace.simple.TracerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TracerServletFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(TracerServletFilter.class);

	private TraceAgent agent;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		agent = new TraceAgent(ConfigUtils.getProperty(TracerConstants.ZIPLIN_SERVER, "http://127.0.0.1:9411"));
		log.info("init the trace filter.");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		Stopwatch watch = Stopwatch.createStarted();

		// start root span
		String uri = req.getRequestURI();
		Span rootSpan = new Span();

		long id = Ids.get();
		rootSpan.setId(id);
		rootSpan.setTrace_id(id);
		rootSpan.setName(uri);
		long timestamp = System.currentTimeMillis();
		rootSpan.setTimestamp(timestamp);

		// sr annotation
		rootSpan.addToAnnotations(
				Annotation.create(timestamp, TracerConstants.ANNO_SR,
						Endpoint.create(uri, ServerInfo.IP4, req.getLocalPort())));

		// prepare trace context
		TracerContext.start();
		TracerContext.setTraceId(rootSpan.getTrace_id());
		TracerContext.setSpanId(rootSpan.getId());
		TracerContext.addSpan(rootSpan);

		// executor other filters
		chain.doFilter(request, response);

		// ss annotation
		rootSpan.addToAnnotations(
				Annotation.create(System.currentTimeMillis(), TracerConstants.ANNO_SS,
						Endpoint.create(rootSpan.getName(), ServerInfo.IP4, req.getLocalPort())));

		rootSpan.setDuration(watch.stop().elapsed(TimeUnit.MICROSECONDS));

		// send trace spans
		agent.send(TracerContext.getSpans());
	}

	@Override
	public void destroy() {
		TracerContext.clear();
	}

}
