package g3.yeepaay.com.yop_android.client;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import g3.yeepaay.com.yop_android.encrypt.AESEncrypt;
import g3.yeepaay.com.yop_android.encrypt.BlowfishEncrypter;
import g3.yeepaay.com.yop_android.encrypt.Digest;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/14 下午4:19
 */
public class Yopclient {

    private static Boolean encrypt;
    private static String signName;
    private static String appKey;
    private static String secretKey;
    private static String customerNo;
    private static String encryptParams;
    private static String signParams;
    private static String signRet;
    private static String serverUrl;

    private static String postUrl;

    public static String getPostUrl() {
        return postUrl;
    }

    public static void setPostUrl(String postUrl) {
        Yopclient.postUrl = postUrl;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static void setServerUrl(String serverUrl) {
        Yopclient.serverUrl = serverUrl;
    }

    public static String isSignRet() {
        return signRet;
    }

    public static void setSignRet(String signRet) {
        Yopclient.signRet = signRet;
    }

    public static String getCustomerNo() {
        return customerNo;
    }

    public static void setCustomerNo(String customerNo) {
        Yopclient.customerNo = customerNo;
    }

    public static String getAppKey() {
        return appKey;
    }

    public static void setAppKey(String appKey) {
        Yopclient.appKey = appKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public  static void setSecretKey(String secretKey) {
        Yopclient.secretKey = secretKey;
    }

    public static Boolean getEncrypt() {
        return encrypt;
    }

    public static void setEncrypt(Boolean encrypt) {
        Yopclient.encrypt = encrypt;
    }

    public static String getSignName() {
        return signName;
    }

    public static void setSignName(String signName) {
        Yopclient.signName = signName;
    }

    private static HashMap<String,String>map=new HashMap<String, String>();
    public static String postData(String methodUrl, Map params){
        signAndEncrypt(params);
        HashMap<String,String>postMap=new HashMap<String, String>();
        postMap.put(YopConstants.TIMESTAMP,map.get(YopConstants.TIMESTAMP));
        postMap.put(YopConstants.LOCALE,"zh_CN");
        postMap.put(YopConstants.FORMAT,"json");
        postMap.put(YopConstants.VERSION,"1.0");
//        postMap.put("signRet","true");
        postMap.put("sign",signParams);
        if(getEncrypt()){
            postMap.put("encrypt",encryptParams);
        }else {
            postMap.putAll(params);
        }
        if(!(getAppKey().isEmpty())){
            postMap.put(YopConstants.APP_KEY,getAppKey());}
        if(!(getCustomerNo().isEmpty())){
            postMap.put(YopConstants.CUSTOMER_NO,getCustomerNo());
        }
        List<String>paramsName=new ArrayList<String>();
        RequestBody requestBody=null;
        String result=null;
        String responseResult=null;
        paramsName.addAll(postMap.keySet());
        FormBody.Builder builder=new FormBody.Builder();
        for(String param:paramsName){
            builder.add(param,postMap.get(param));
        }
        requestBody=builder.build();
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request=new Request.Builder().url(serverUrl+methodUrl).post(requestBody).build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            responseResult=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(getEncrypt()){
            result=decryptParams(responseResult);
            return result;
        }
         return responseResult;
    }


    /**
     * 对参数进行签名和加密
     * @param params
     */
    public static void signAndEncrypt(Map params){
        if(getAppKey().isEmpty()&&getCustomerNo().isEmpty()){
            System.out.println("appKey和customerNo不能同时为空");
        }
        if(!(getAppKey().isEmpty())){
            map.put(YopConstants.APP_KEY,getAppKey());
        }
        if(!(getCustomerNo().isEmpty())){
            map.put(YopConstants.CUSTOMER_NO,getCustomerNo());
        }
        map.put(YopConstants.TIMESTAMP,String.valueOf(System.currentTimeMillis()));
        map.put(YopConstants.LOCALE,"zh_CN");
        map.put(YopConstants.FORMAT,"json");
        map.put(YopConstants.VERSION,"1.0");
        map.put(YopConstants.METHOD,getPostUrl());
        map.putAll(params);
        if(isSignRet().equals("true")){
            signParams=sign(map,getSignName());
        }else if(isSignRet().equals("false")){
            signParams="";
        }
        if(getEncrypt()){
            encryptParams=encryptParams(params);
        }else{
            encryptParams="";
        }
    }

    /**
     * 对参数进行签名
     * @return
     */
    public static String sign(Map <String,String>signParams,String algName){
        if(algName.isEmpty()){
            algName="SHA-256";
        }
        StringBuilder builder=new StringBuilder();
        List<String>paramNames=new ArrayList<String>(signParams.size());
        paramNames.addAll(signParams.keySet());
        Collections.sort(paramNames);
        builder.append(Yopclient.getSecretKey());
        for(String paramName:paramNames){
            try {
//                builder.append(paramName).append(URLEncoder.encode(signParams.get(paramName),YopConstants.ENCODING));
                builder.append(paramName).append(signParams.get(paramName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        builder.append(Yopclient.getSecretKey());
        return Digest.digest(builder.toString(),algName);
    }

    /**
     * 对参数进行加密，APPKey形式的进行AES加密，customerNo形式进行blowfish加密
     * @param Params
     * @return
     */
    public static String encryptParams(Map <String,String>Params){
        List<String>keys=new ArrayList<String>(Params.size());
        Map<String,String>encryptParams=new TreeMap<String, String>();
        String encryptBody=null;
        String encrypt=null;
        keys.addAll(Params.keySet());
        for(String key:keys){
            if(YopConstants.isProtectedKey(key)){
                keys.remove(key);
            }
        }
        Collections.sort(keys);
        for(String key:keys){
            try {
                encryptParams.put(key,URLEncoder.encode(Params.get(key), YopConstants.ENCODING));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        encryptBody = Joiner.on("&").withKeyValueSeparator("=").join(encryptParams);
        if(!getAppKey().isEmpty()){
            encrypt= AESEncrypt.encrypt(encryptBody,getSecretKey());
        }
        else if(!getCustomerNo().isEmpty()){
            encrypt= BlowfishEncrypter.encrypt(encryptBody,getSecretKey());
        }
        return encrypt;
    }

    /**
     * 对加密的内容进行解密，appkey对应AES解密，customerNo对应Blowfish解密
     * @param encryptString
     * @return
     */
    public static String decryptParams(String encryptString){
        JSONTokener jsonParser = new JSONTokener(encryptString);
        String decryptResult=null;
        StringBuilder stringBuilder=new StringBuilder();
        try {
            JSONObject jsonObject=(JSONObject) jsonParser.nextValue();
            String encryptResult=(String)jsonObject.get("result");
            String state=(String)jsonObject.get("state");
            String ts=String.valueOf(jsonObject.get("ts"));
            if(!(getAppKey().isEmpty())){
                decryptResult=AESEncrypt.decrypt(encryptResult,getAppKey());
            }else if(!(getCustomerNo().isEmpty())){
                decryptResult=BlowfishEncrypter.decrypt(encryptResult,getCustomerNo());
            }
            stringBuilder.append("state:").append(state).append("\n").append("result:").append(decryptResult)
                    .append("\n").append("ts:").append(ts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
