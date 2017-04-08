package com.hikvision.ga.trace.log;

/**
 * 
 * @author fangzhibin
 *
 */
public class TraceContext {

	/**
	 * The trace chain global id
	 */
	private static ThreadLocal<TraceTuple> traceTuple = new InheritableThreadLocal<TraceTuple>();

	private TraceContext() {
	}

	public static TraceTuple get() {
		TraceTuple tt = traceTuple.get();
		if(null == tt) {
			tt = new TraceTuple();
			traceTuple.set(tt);
		}
		return tt;
	}

	public static void set(final TraceTuple tt) {
		traceTuple.set(tt);
	}

	public static void clear() {
		traceTuple.remove();
	}
}
