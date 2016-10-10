package g3.yeepaay.com.yop_android.encrypt;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/14 下午3:02
 */
public class Hex {
    public static String toHex(byte input[]) {
        if (input == null)
            return null;
        StringBuffer output = new StringBuffer(input.length * 2);
        for (int i = 0; i < input.length; i++) {
            int current = input[i] & 0xff;
            if (current < 16)
                output.append("0");
            output.append(Integer.toString(current, 16));
        }
        return output.toString();
    }

    public static byte[] fromHex(String input) {
        if (input == null)
            return null;
        byte output[] = new byte[input.length() / 2];
        for (int i = 0; i < output.length; i++)
            output[i] = (byte) Integer.parseInt(
                    input.substring(i * 2, (i + 1) * 2), 16);
        return output;
    }
}
