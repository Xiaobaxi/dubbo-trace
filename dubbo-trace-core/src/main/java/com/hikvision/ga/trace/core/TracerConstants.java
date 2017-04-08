package com.hikvision.ga.trace.core;

/**
 * Trace constants
 * 
 * @author fangzhibin
 *
 */
public class TracerConstants {

	/**
	 * ot span
	 */
	public static final String ACTIVE_SPAN = "ot_active_span";
	
	/**
	 * ot tracer
	 */
	public static final String ACTIVE_TRACER = "ot_tracer";

	/**
	 * trace enable
	 */
	public static final String TRACE_ENABLED = "trace.enabled";

	/**
	 * trace type like [simple ot brave]
	 */
	public static final String TRACE_TYPE = "trace.type";
	
	/**
	 * zipkin server
	 */
	public static final String ZIPLIN_SERVER = "trace.zipkin.server";

	/**
	 * version no
	 */
	public static final String VERSION_NO = "VOA";

	/**
	 * Trace id
	 */
	public static final String TRACE_ID = "traceId";

	/**
	 * Span id
	 */
	public static final String SPAN_ID = "spanId";

	/**
	 * parent id, the span id for the parent
	 */
	public static final String PARENT_ID = "parentId";
	/**
	 * The cs annotation
	 */
	public static final String ANNO_CS = "cs";

	/**
	 * The cr annotation
	 */
	public static final String ANNO_CR = "cr";

	/**
	 * The sr annotation
	 */
	public static final String ANNO_SR = "sr";

	/**
	 * The ss annotation
	 */
	public static final String ANNO_SS = "ss";

	/**
	 * The sr time
	 */
	public static final String SR_TIME = "srt";

	/**
	 * The ss time
	 */
	public static final String SS_TIME = "sst";

}
