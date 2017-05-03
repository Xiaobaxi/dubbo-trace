package com.hikvision.ga.trace.core;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 
 * @author fangzhibin
 *
 */
public interface TracerClient {

	public Result invoke(Invoker<?> invoker, Invocation invocation, boolean isProvider) throws RpcException;
	
}
