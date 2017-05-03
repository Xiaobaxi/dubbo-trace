package com.hikvision.ga.trace.simple;

import com.github.kristofa.brave.SpanCollectorMetricsHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author fangzhibin
 *
 */
public class SimpleMetricsHandler implements SpanCollectorMetricsHandler {

	final AtomicInteger acceptedSpans = new AtomicInteger();
	final AtomicInteger droppedSpans = new AtomicInteger();

	@Override
	public void incrementAcceptedSpans(int quantity) {
		acceptedSpans.addAndGet(quantity);
	}

	@Override
	public void incrementDroppedSpans(int quantity) {
		droppedSpans.addAndGet(quantity);
	}
}