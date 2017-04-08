package com.hikvision.ga.trace.core;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 
 * @author fangzhibin
 *
 */
@Activate(group = Constants.PROVIDER)
public class TracerProviderFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		String enabled = ConfigUtils.getProperty(TracerConstants.TRACE_ENABLED, Boolean.FALSE.toString());
    	if(Boolean.FALSE.toString().equalsIgnoreCase(enabled)) {
    		return invoker.invoke(invocation);
    	}
		TracerRegistryFactory trf = new TracerRegistryFactory();
		return trf.getTracerClient().invoke(invoker, invocation, true);
	}

}
