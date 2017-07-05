package io.github.xiaobaxi.trace.filter.support;

import io.opentracing.NoopTracerFactory;
import io.opentracing.Span;
import io.opentracing.Tracer;

/**
 * Created by xiaobaxi on 2017/7/5.
 */
public interface TracerFactory {

    TracerFactory DEFAULT = new DefaultTraceFactory();


    Tracer getTracer();


    String getTraceId(Span span);


    class DefaultTraceFactory implements TracerFactory {
        private static Tracer tracer =  NoopTracerFactory.create();

        public Tracer getTracer() {
            return tracer;
        }

        public String getTraceId(Span span) {
            return "";
        }

    }
}
