package io.github.xiaobaxi.trace.filter.support;

import brave.Tracing;
import brave.opentracing.BraveSpanContext;
import brave.opentracing.BraveTracer;
import io.opentracing.Tracer;
import zipkin.Span;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.okhttp3.OkHttpSender;

/**
 * Created by xiaobaxi on 2017/7/5.
 */
public class ZipkinTracerFactory implements TracerFactory {

    private static Tracer tracer = null;

    static {
        OkHttpSender sender = OkHttpSender.create("http://127.0.0.1:9411/api/v1/spans");
        AsyncReporter<Span> reporter = AsyncReporter.builder(sender).build();

        Tracing braveTracing = Tracing.newBuilder()
                .reporter(reporter)
                .build();

        Tracer tracer = BraveTracer.create(braveTracing);

        ZipkinTracerFactory.tracer = tracer;

    }

    @Override
    public Tracer getTracer() {
        return tracer;
    }

    @Override
    public String getTraceId(io.opentracing.Span span) {
        return ((BraveSpanContext) span.context()).unwrap().traceIdString();
    }
}
