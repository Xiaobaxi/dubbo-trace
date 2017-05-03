package com.hikvision.ga.trace.opentracing.support;

import java.util.Iterator;
import java.util.Map.Entry;

import io.opentracing.NoopTracer;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.propagation.Format;
import io.opentracing.propagation.TextMap;
import io.opentracing.propagation.TextMapExtractAdapter;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.hikvision.ga.trace.core.TracerClient;
import com.hikvision.ga.trace.opentracing.OpenTracingContext;

/**
 * 
 * @author fangzhibin
 *
 */
public class OpenTracingClient implements TracerClient {
	
	private static final Logger logger = LoggerFactory.getLogger(OpenTracingClient.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation, boolean isProvider) throws RpcException {
    	Span span = null;
		if(isProvider) {
			String serviceName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
			Tracer tracer = OpenTracingContext.buildTracer(serviceName);
			if (tracer == null || tracer instanceof NoopTracer) {
				return invoker.invoke(invocation);
			}
			OpenTracingContext.setTracer(tracer);
			String operationName = invoker.getInterface().getName() + "." + invocation.getMethodName();
			SpanBuilder spanBuilder = tracer.buildSpan(operationName);
			try {
				SpanContext spanContext = tracer.extract(Format.Builtin.TEXT_MAP, new TextMapExtractAdapter(invocation.getAttachments()));
				if (spanContext != null) {
					spanBuilder.asChildOf(spanContext);
				}
			} catch (Exception e) {
				spanBuilder.withTag("Error", "extract from request fail, error msg:" + e.getMessage());
			}
			span = spanBuilder.start();
			span.setTag("requestId", invocation.hashCode());
			OpenTracingContext.setActiveSpan(span);
		} else {
			Tracer tracer = OpenTracingContext.getTracer();
			if (tracer == null || tracer instanceof NoopTracer) {
				return invoker.invoke(invocation);
			}
			String operationName = invoker.getInterface().getName() + "." + invocation.getMethodName();
			SpanBuilder spanBuilder = tracer.buildSpan(operationName);
			Span activeSpan = OpenTracingContext.getActiveSpan();
	        if (activeSpan != null) {
	        	logger.debug("the parent span is exist");
	            spanBuilder.asChildOf(activeSpan);
	        }
	        span = spanBuilder.start();
	        span.setTag("requestId", invoker.hashCode());
	        attachTraceInfo(tracer, span, invocation);
		}
        return process(invoker, invocation, span);
	}
	
	protected Result process(Invoker<?> invoker, Invocation invocation, Span span) {
        Throwable ex = null;
        boolean exception = true;
        try {
        	Result result = invoker.invoke(invocation);
            if (result.getException() != null) {
                ex = result.getException();
            } else {
                exception = false;
            }
            return result;
        } catch (RuntimeException e) {
            ex = e;
            throw e;
        } finally {
            try {
                if (exception) {
                    span.log("request fail." + (ex == null ? "unknown exception" : ex.getMessage()));
                } else {
                    span.log("request success.");
                }
                span.finish();
            } catch (Exception e) {
            	logger.error("opentracing span finish error!", e);
            }
        }
    }
	
	protected void attachTraceInfo(Tracer tracer, Span span, final Invocation invocation) {
        tracer.inject(span.context(), Format.Builtin.TEXT_MAP, new TextMap() {

            @Override
            public void put(String key, String value) {
            	invocation.setAttachment(key, value);
            }

            @Override
            public Iterator<Entry<String, String>> iterator() {
                throw new UnsupportedOperationException("TextMapInjectAdapter should only be used with Tracer.inject()");
            }
        });
    }

}
