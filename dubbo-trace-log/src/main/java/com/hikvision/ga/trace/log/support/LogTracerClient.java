package com.hikvision.ga.trace.log.support;

import java.util.UUID;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.hikvision.ga.trace.core.TracerClient;
import com.hikvision.ga.trace.core.TracerConstants;
import com.hikvision.ga.trace.log.TraceContext;
import com.hikvision.ga.trace.log.TraceInfoUtils;
import com.hikvision.ga.trace.log.TraceTuple;

/**
 * 
 * @author fangzhibin
 *
 */
public class LogTracerClient implements TracerClient {
	
	private static final Logger logger = LoggerFactory.getLogger(LogTracerClient.class);

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation, boolean isProvider) throws RpcException {
    	RpcContext rpcContext = RpcContext.getContext();
		String traceId = rpcContext.getAttachment(TracerConstants.TRACE_ID);
		if (StringUtils.isEmpty(traceId)) {
			traceId = TraceContext.get().getTraceId();
		}
		if (StringUtils.isEmpty(traceId)) {
			traceId = TracerConstants.VERSION_NO + UUID.randomUUID().toString().replace("-", "");
		}

		String parentId = rpcContext.getAttachment(TracerConstants.PARENT_ID);
		parentId = (parentId == null) ? "" : parentId;

		String spanId = UUID.randomUUID().toString().replace("-", "");

		String logInfo = TraceInfoUtils.buildTraceInfo(invocation, traceId, spanId, parentId);
		logger.debug(logInfo);
		
		TraceTuple tt = TraceContext.get();
		tt.setTraceId(traceId);
		tt.setParentId(parentId);
		tt.setSpanId(spanId);
		rpcContext.setAttachment(TracerConstants.TRACE_ID, traceId);
		rpcContext.setAttachment(TracerConstants.PARENT_ID, spanId);
		
		Result result = invoker.invoke(invocation);
		return result;
	}

}
