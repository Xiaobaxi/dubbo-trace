package io.github.xiaobaxi.trace.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.*;
import io.github.xiaobaxi.trace.filter.support.OpenTracingContext;
import io.github.xiaobaxi.trace.filter.support.TracerFactory;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by xiaobaxi on 2017/7/5.
 */
@Activate(group = {"provider", "consumer"})
public class OpenTracingFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(OpenTracingFilter.class);

    static {
        Iterator<TracerFactory> iterator = ServiceLoader.load(TracerFactory.class).iterator();
        if (iterator.hasNext()) {
            TracerFactory tracerFactory = iterator.next();
            OpenTracingContext.setTracerFactory(tracerFactory);
        } else {
            //just for test
            //OpenTracingContext.setTracerFactory(new BraveTracerFactory());
        }
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return null;
    }
}
