package com.hikvision.ga.trace.simple.support;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.hikvision.ga.trace.core.TracerClient;
import com.hikvision.ga.trace.core.TracerConstants;
import com.hikvision.ga.trace.simple.Ids;
import com.hikvision.ga.trace.simple.Networks;
import com.hikvision.ga.trace.simple.TraceAgent;
import com.hikvision.ga.trace.simple.TracerContext;
import com.twitter.zipkin.gen.Annotation;
import com.twitter.zipkin.gen.BinaryAnnotation;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;

public class SimpleTracerClient implements TracerClient {

	private TraceAgent agent = new TraceAgent(ConfigUtils.getProperty(TracerConstants.ZIPLIN_SERVER, "http://127.0.0.1:9411"));

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation, boolean isProvider) throws RpcException {
		Result result = null;
		if(isProvider) {
			Map<String, String> attaches = invocation.getAttachments();
			if (!attaches.containsKey(TracerConstants.TRACE_ID)) {
				return invoker.invoke(invocation);
			}
			
			Long traceId = Long.parseLong(attaches.get(TracerConstants.TRACE_ID));
			Long parentSpanId = Long.parseLong(attaches.get(TracerConstants.SPAN_ID));

			TracerContext.start();
			TracerContext.setTraceId(traceId);
			TracerContext.setSpanId(parentSpanId);
			result = invoker.invoke(invocation);
			agent.send(TracerContext.getSpans());
			TracerContext.clear();
		} else {
			if (TracerContext.getTraceId() == null){
	            return invoker.invoke(invocation);
	        }
	        Stopwatch watch = Stopwatch.createStarted();
	        Span consumeSpan = new Span();
			consumeSpan.setId(Ids.get());
			Long traceId = TracerContext.getTraceId();
			Long parentId = TracerContext.getSpanId();
			consumeSpan.setTrace_id(traceId);
			consumeSpan.setParent_id(parentId);
			String serviceName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
			consumeSpan.setName(serviceName);
			long timestamp = System.currentTimeMillis();
			consumeSpan.setTimestamp(timestamp);

			// cs annotation
			URL provider = invoker.getUrl();
			int providerHost = Networks.ip2Num(provider.getHost());
			int providerPort = provider.getPort();
			consumeSpan.addToAnnotations(Annotation.create(timestamp, TracerConstants.ANNO_CS,
					Endpoint.create(serviceName, providerHost, providerPort)));

			String providerOwner = provider.getParameter("owner");
			if (!Strings.isNullOrEmpty(providerOwner)) {
				consumeSpan.addToBinary_annotations(BinaryAnnotation.create("owner", providerOwner, null));
			}

			Map<String, String> attaches = invocation.getAttachments();
			attaches.put(TracerConstants.TRACE_ID, String.valueOf(consumeSpan.getTrace_id()));
			attaches.put(TracerConstants.SPAN_ID, String.valueOf(consumeSpan.getId()));
	        result = invoker.invoke(invocation);
	        consumeSpan.setDuration(watch.stop().elapsed(TimeUnit.MICROSECONDS));

			// cr annotation
			URL url = invoker.getUrl();
			consumeSpan.addToAnnotations(Annotation.create(System.currentTimeMillis(), TracerConstants.ANNO_CR,
					Endpoint.create(consumeSpan.getName(), Networks.ip2Num(url.getHost()), url.getPort())));

			Throwable throwable = result.getException();
			if (throwable != null) {
				consumeSpan.addToBinary_annotations(BinaryAnnotation.create("Exception", Throwables.getStackTraceAsString(throwable), null));
			}
			// collect the span
			TracerContext.addSpan(consumeSpan);
		}
		
		return result;
	}

}
