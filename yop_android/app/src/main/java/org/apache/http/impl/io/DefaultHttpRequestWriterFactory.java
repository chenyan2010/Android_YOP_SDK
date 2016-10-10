//package org.apache.http.impl.io;
//
//import org.apache.http.HttpRequest;
//import org.apache.http.io.HttpMessageWriter;
//import org.apache.http.io.HttpMessageWriterFactory;
//import org.apache.http.io.SessionOutputBuffer;
//import org.apache.http.message.BasicLineFormatter;
//import org.apache.http.message.LineFormatter;
//
///**
// * title: <br/>
// * description:描述<br/>
// * Copyright: Copyright (c)2016<br/>
// * Company: 易宝支付(YeePay)<br/>
// *
// * @author guoliang.li
// * @version 1.0.0
// * @since 16/9/27 上午10:01
// */
//public class DefaultHttpRequestWriterFactory implements HttpMessageWriterFactory<HttpRequest> {
//    public static final DefaultHttpRequestWriterFactory INSTANCE = new DefaultHttpRequestWriterFactory();
//    private final LineFormatter lineFormatter;
//
//    public DefaultHttpRequestWriterFactory(LineFormatter lineFormatter) {
//        this.lineFormatter = (LineFormatter)(lineFormatter != null?lineFormatter: BasicLineFormatter.DEFAULT);
//    }
//
//    public DefaultHttpRequestWriterFactory() {
//        this((LineFormatter)null);
//    }
//
//    public HttpMessageWriter<HttpRequest> create(SessionOutputBuffer buffer) {
//        return new DefaultHttpRequestWriter(buffer, this.lineFormatter);
//    }
//}
//
