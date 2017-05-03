package com.hikvision.ga.trace.log;

import com.alibaba.dubbo.rpc.Invocation;

/**
 * 
 * @author fangzhibin
 *
 */
public class TraceInfoUtils {

	public TraceInfoUtils() {
	}

	public static String buildTraceInfo(Invocation invocation, String... ids) {
		StringBuilder sb = new StringBuilder();
		sb.append("[traceId=").append(ids[0]).append(",");
		sb.append("parentId=").append(ids[2]).append(",");
		sb.append("spanId=").append(ids[1]).append("]");

		sb.append(" ");
		sb.append(buildIntfSignature(invocation));
		sb.append(" ");
		sb.append(buildIntfParams(invocation));
		return sb.toString();
	}

	private static String buildIntfSignature(Invocation invocation) {
		StringBuilder sb = new StringBuilder();
		sb.append(invocation.getInvoker().getInterface().getSimpleName()).append(".").append(invocation.getMethodName()).append("(");
		Class<?> types[] = invocation.getParameterTypes();
		if (null != types && types.length > 0) {
			for (int i = 0, l = types.length; i < l; i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(types[i].getName());
			}
		}
		sb.append(")");
		return sb.toString();
	}

	private static String buildIntfParams(Invocation invocation) {
		StringBuilder sb = new StringBuilder();
		Object[] params = invocation.getArguments();
		if (null != params && params.length > 0) {
			for (int i = 0, l = params.length; i < l; i++) {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(params[i]);
			}
		}
		return sb.toString();
	}

}