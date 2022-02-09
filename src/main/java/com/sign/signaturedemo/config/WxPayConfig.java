package com.sign.signaturedemo.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

/**
 * 调用微信封装接口实现生成签名、验签
 *
 * @author 刘雪
 * @since 2021/12/24 16:16:32
 */
@Component
public class WxPayConfig {

    // 私钥地址
    private static final String privateKey = "/Users/xuhongzhi/Desktop/签名验签demo示例/Signature-demo/sign/pri.pem";

    private static final String apiV3Key = "";

    // 商户号
    private static final String mchId = "2234555";

    private static final String mchSerialNo = "14444442224"; // 商户证书序列号

    /**
     *
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    private PrivateKey getPrivateKey(String path) {
        try {
            return PemUtil.loadPrivateKey(new FileInputStream(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("私钥文件不存在", e);
        }
    }

    /**
     * 获取商户私钥
     *
     * @return
     */
    public ScheduledUpdateCertificatesVerifier getVerifier() {

        PrivateKey privateKey = getPrivateKey(WxPayConfig.privateKey);
        // 私钥签名对象
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(mchSerialNo, privateKey);
        // 身份认证对象
        WechatPay2Credentials credentials = new WechatPay2Credentials(mchId, privateKeySigner);

        // 使用定时更新的签名验证器，不需要传入证书
        return new ScheduledUpdateCertificatesVerifier(credentials, apiV3Key.getBytes(StandardCharsets.UTF_8));

    }

    public CloseableHttpClient getWxPayClient(ScheduledUpdateCertificatesVerifier verifier) throws FileNotFoundException {
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, getPrivateKey(privateKey))
                .withValidator(new WechatPay2Validator(verifier));
        // 自动处理签名和验签 实现自动更新
        return builder.build();
    }

}
