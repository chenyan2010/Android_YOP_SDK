package g3.yeepaay.com.yop_android.encrypt;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/14 下午2:58
 */
public class BlowfishEncrypter {
//    private final static Logger logger = Logger
//            .getLogger(BlowfishEncrypter.class);

    // ----- 常量 -----
    private static String CIPHER_NAME = "Blowfish/CFB8/NoPadding";
    private static String KEY_SPEC_NAME = "Blowfish";
    private static String CHARSET = "UTF-8";

    private static ConcurrentHashMap<String, BlowfishEncrypter> pool = new ConcurrentHashMap<String, BlowfishEncrypter>();

    private SecretKeySpec secretKeySpec = null;
    private IvParameterSpec ivParameterSpec;

    /**
     * 初始化
     */
    private BlowfishEncrypter(String key) {
        try {
            String md5Key = Digest.md5Digest(key);
            secretKeySpec = new SecretKeySpec(md5Key.substring(0, 16)
                    .getBytes(), KEY_SPEC_NAME);
            ivParameterSpec = new IvParameterSpec(
                    (md5Key.substring(0, 8)).getBytes());
        } catch (Exception e) {
//            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(Base64.encode(cipher.doFinal(data.getBytes(CHARSET))), CHARSET);
    }

    public String decrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(cipher.doFinal(Base64.decode(data.getBytes(CHARSET))), CHARSET);
    }

    private static BlowfishEncrypter getInstance(String key) {
        BlowfishEncrypter b = pool.get(key);
        if (b != null) {
            return b;
        }

        b = new BlowfishEncrypter(key);
        pool.put(key, b);
        return b;

    }

    /**
     * 加密一个字符串。
     *
     * @param data
     * @return 返回加密的字符串，如果加密失败，返回null
     */
    public static String encrypt(String data, String key) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            return null;
        }

        try {
            return getInstance(key).encrypt(data);
        } catch (Exception e) {
//            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密一个字符串。
     *
     * @param data
     * @return 返回解密的字符串，如果解密失败，返回null
     */
    public static String decrypt(String data, String key) {
        if (StringUtils.isBlank(data) || StringUtils.isBlank(key)) {
            return null;
        }

        try {
            return getInstance(key).decrypt(data);
        } catch (Exception e) {
//            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}


