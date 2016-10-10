package g3.yeepaay.com.yop_android.exception;

/**
 * title: <br/>
 * description:描述<br/>
 * Copyright: Copyright (c)2016<br/>
 * Company: 易宝支付(YeePay)<br/>
 *
 * @author guoliang.li
 * @version 1.0.0
 * @since 16/9/14 下午3:05
 */
public class YopClientException extends RuntimeException{
    private static final long serialVersionUID = -9085416005820812953L;

    /**
     * Constructs a new YopClientException with the specified detail message.
     *
     * @param message the detail error message.
     */
    public YopClientException(String message) {
        super(message);
    }

    /**
     * Constructs a new YopClientException with the specified detail message and the underlying cause.
     *
     * @param message the detail error message.
     * @param cause   the underlying cause of this exception.
     */
    public YopClientException(String message, Throwable cause) {
        super(message, cause);
    }

}
