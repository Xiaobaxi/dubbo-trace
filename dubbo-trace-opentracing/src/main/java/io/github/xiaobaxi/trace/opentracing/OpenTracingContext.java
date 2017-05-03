package com.hikvision.ga.trace.opentracing;

import java.util.concurrent.locks.ReentrantLock;

import io.opentracing.Span;
import io.opentracing.Tracer;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.RpcContext;
import com.hikvision.ga.trace.core.TracerConstants;

/**
 * OpenTracingContext hold the span and tracer
 * 
 * @author fangzhibin
 *
 */
public class OpenTracingContext {

	private static final ReentrantLock LOCK = new ReentrantLock();

	public static Tracer buildTracer(String serverName) {
		try {
			LOCK.lock();
			TracerBuilder tb = ExtensionLoader.getExtensionLoader(TracerBuilder.class).getDefaultExtension();
			Tracer tracer = tb.build(serverName);
			return tracer;
		} finally {
			LOCK.unlock();
		}
	}

	public static Tracer getTracer() {
		Object tracer = RpcContext.getContext().get(TracerConstants.ACTIVE_TRACER);
		if (tracer != null && tracer instanceof Tracer) {
			return (Tracer) tracer;
		}
		return null;
	}

	public static void setTracer(Tracer tracer) {
		RpcContext.getContext().set(TracerConstants.ACTIVE_TRACER, tracer);
	}

	public static Span getActiveSpan() {
		Object span = RpcContext.getContext().get(TracerConstants.ACTIVE_SPAN);
		if (span != null && span instanceof Span) {
			return (Span) span;
		}
		return null;
	}

	public static void setActiveSpan(Span span) {
		RpcContext.getContext().set(TracerConstants.ACTIVE_SPAN, span);
	}

}
