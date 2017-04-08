package com.hikvision.ga.trace.simple.support;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.google.common.base.Stopwatch;
import com.hikvision.ga.trace.core.TracerConstants;
import com.hikvision.ga.trace.core.TracerServletClient;
import com.hikvision.ga.trace.simple.Ids;
import com.hikvision.ga.trace.simple.ServerInfo;
import com.hikvision.ga.trace.simple.TraceAgent;
import com.hikvision.ga.trace.simple.TracerContext;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author fangzhibin
 *
 */
public class SimpleServletClient implements TracerServletClient {

    private static final Logger log = LoggerFactory.getLogger(SimpleServletClient.class);

    private TraceAgent agent;

    @Override
    public void init(FilterConfig config) throws ServletException {
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
