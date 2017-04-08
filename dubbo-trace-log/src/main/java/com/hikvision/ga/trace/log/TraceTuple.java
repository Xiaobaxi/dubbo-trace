package com.hikvision.ga.trace.log;

/**
 * trace tuple
 * @author fangzhibin
 *
 */
public class TraceTuple {
	
	private String traceId;
	
	private String spanId;
	
	private String parentId;

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getSpanId() {
		return spanId;
	}

	public void setSpanId(String spanId) {
		this.spanId = spanId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
