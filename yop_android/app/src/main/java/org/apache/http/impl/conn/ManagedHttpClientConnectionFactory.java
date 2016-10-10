//package org.apache.http.impl.conn;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.apache.http.HttpRequest;
//import org.apache.http.HttpResponse;
//import org.apache.http.config.ConnectionConfig;
//import org.apache.http.conn.HttpConnectionFactory;
//import org.apache.http.conn.ManagedHttpClientConnection;
//import org.apache.http.conn.routing.HttpRoute;
//import org.apache.http.entity.ContentLengthStrategy;
//import org.apache.http.impl.entity.LaxContentLengthStrategy;
//import org.apache.http.impl.entity.StrictContentLengthStrategy;
//import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
//import org.apache.http.io.HttpMessageParserFactory;
//import org.apache.http.io.HttpMessageWriterFactory;
//
//import java.nio.charset.Charset;
//import java.nio.charset.CharsetDecoder;
//import java.nio.charset.CharsetEncoder;
//import java.nio.charset.CodingErrorAction;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * title: <br/>
// * description:描述<br/>
// * Copyright: Copyright (c)2016<br/>
// * Company: 易宝支付(YeePay)<br/>
// *
// * @author guoliang.li
// * @version 1.0.0
// * @since 16/9/27 下午2:23
// */
//public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> {
//    private static final AtomicLong COUNTER = new AtomicLong();
//    public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
//    private final Log log;
//    private final Log headerlog;
//    private final Log wirelog;
//    private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
//    private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
//    private final ContentLengthStrategy incomingContentStrategy;
//    private final ContentLengthStrategy outgoingContentStrategy;
//
//    public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
//        this.log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
//        this.headerlog = LogFactory.getLog("org.apache.http.headers");
//        this.wirelog = LogFactory.getLog("org.apache.http.wire");
//        this.requestWriterFactory = (HttpMessageWriterFactory)(requestWriterFactory != null?requestWriterFactory:DefaultHttpRequestWriterFactory.INSTANCE);
//        this.responseParserFactory = (HttpMessageParserFactory)(responseParserFactory != null?responseParserFactory:DefaultHttpResponseParserFactory.INSTANCE);
//        this.incomingContentStrategy = (ContentLengthStrategy)(incomingContentStrategy != null?incomingContentStrategy:new LaxContentLengthStrategy());
//        this.outgoingContentStrategy = (ContentLengthStrategy)(outgoingContentStrategy != null?outgoingContentStrategy: new StrictContentLengthStrategy());
//    }
//
//    public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
//        this(requestWriterFactory, responseParserFactory, (ContentLengthStrategy)null, (ContentLengthStrategy)null);
//    }
//
//    public ManagedHttpClientConnectionFactory(HttpMessageParserFactory<HttpResponse> responseParserFactory) {
//        this((HttpMessageWriterFactory)null, responseParserFactory);
//    }
//
//    public ManagedHttpClientConnectionFactory() {
//        this((HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
//    }
//
//    public ManagedHttpClientConnection create(HttpRoute route, ConnectionConfig config) {
//        ConnectionConfig cconfig = config != null?config:ConnectionConfig.DEFAULT;
//        CharsetDecoder chardecoder = null;
//        CharsetEncoder charencoder = null;
//        Charset charset = cconfig.getCharset();
//        CodingErrorAction malformedInputAction = cconfig.getMalformedInputAction() != null?cconfig.getMalformedInputAction():CodingErrorAction.REPORT;
//        CodingErrorAction unmappableInputAction = cconfig.getUnmappableInputAction() != null?cconfig.getUnmappableInputAction():CodingErrorAction.REPORT;
//        if(charset != null) {
//            chardecoder = charset.newDecoder();
//            chardecoder.onMalformedInput(malformedInputAction);
//            chardecoder.onUnmappableCharacter(unmappableInputAction);
//            charencoder = charset.newEncoder();
//            charencoder.onMalformedInput(malformedInputAction);
//            charencoder.onUnmappableCharacter(unmappableInputAction);
//        }
//
//        String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
//        return new LoggingManagedHttpClientConnection(id, this.log, this.headerlog, this.wirelog, cconfig.getBufferSize(), cconfig.getFragmentSizeHint(), chardecoder, charencoder, cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
//    }
//}
//
