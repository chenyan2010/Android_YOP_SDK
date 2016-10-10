package testYop;


import org.junit.Test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import g3.yeepaay.com.yop_android.client.Yopclient;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/22 下午3:54
 */
public class testPost {
    @Test
    public void testPostData(){
        Map<String, String> map = new HashMap<String, String>();
        Yopclient.setAppKey("B112345678901237");
        Yopclient.setCustomerNo("10040011444");
        Yopclient.setSecretKey("nUXQx0Mt0aSKvR0uNOp6kg==");
        Yopclient.setServerUrl("http://10.151.30.88:8064/yop-center");
        Yopclient.setSignRet("true");
        Yopclient.setSignName("SHA1");
        Yopclient.setEncrypt(false);
        Yopclient.setPostUrl("/rest/v1.0/member/queryAccount");
        map.put("requestId", "124");
        map.put("platformUserNo", "8880222");
        String result= Yopclient.postData("/rest/v1.0/member/queryAccount", map);
        System.out.println(result);
    }
}
