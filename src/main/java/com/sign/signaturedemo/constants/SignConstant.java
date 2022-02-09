package com.sign.signaturedemo.constants;

import okhttp3.internal.publicsuffix.PublicSuffixDatabase;

import java.security.SecureRandom;

/**
 * @author 刘雪
 * @since 2021/12/27 10:52:37
 */
public class SignConstant {

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String SCHEMA = "TJLC-SHA256-RSA2048";

    public static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final SecureRandom RANDOM = new SecureRandom();

    // 私钥地址
    public static final String PRIVATE_KEY = "/Users/xuhongzhi/Desktop/签名验签demo示例/Signature-demo/sign/pri.pem";

    // 公钥地址
    public static final String PUBLIC_KEY = "/Users/xuhongzhi/Desktop/签名验签demo示例/Signature-demo/sign/pub.pem";

    // 商户号
    public static final String APP_ID = "2234555";

    // 加密方式
    public static final String SHA256_RSA = "SHA256withRSA";

    public static final String SECRET_KEY = "xeB11h7ooQkcpG7G";

    public static final String RSA = "RSA";

    public static final String TIMESTAMP = "timestamp";

    public static final String NONCE_STR = "nonce_str";

    public static final String SIGNATURE = "signature";

    public static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----";

    public static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";

    public static final String BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----";

    public static final String END_PUBLIC_KEY = "-----END PUBLIC KEY-----";

}
