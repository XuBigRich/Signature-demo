package com.sign.signaturedemo.controller;

import com.alibaba.fastjson.JSON;
import com.sign.signaturedemo.config.WxPayConfig;
import com.wechat.pay.contrib.apache.httpclient.auth.ScheduledUpdateCertificatesVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用微信封装接口实现生成签名、验签的控制层(需要微信平台的apiV3Key、商户号、商户证书序列号)
 *
 * @author 刘雪
 * @since 2021/12/24 16:38:17
 */
@Slf4j
@RestController
@RequestMapping("/wx-pay")
public class WxPayController {

    @Autowired
    private WxPayConfig wxPayConfig;

    // 请求地址
    private static final String url = "";

    @PostMapping("/test")
    public Map<String, Object> test() throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity("", "utf-8");
        stringEntity.setContentType("application/json");
        httpPost.setHeader("Accent", "application/json");
        // 执行请求
        ScheduledUpdateCertificatesVerifier verifier = wxPayConfig.getVerifier();
        CloseableHttpClient httpClient = wxPayConfig.getWxPayClient(verifier);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            // 响应体
            String body = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("success, body = " + body);
            } else if (statusCode == 204) {  // 处理成功无返回body
                log.info("body");
            } else {
                System.out.println("failed, resp code = " + statusCode + ", return body = " + body);
                throw new IOException("请求失败");
            }
            // 响应结果
            return JSON.parseObject(body, HashMap.class);
        } finally {
            response.close();
        }
    }

}
