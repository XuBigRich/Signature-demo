package com.sign.signaturedemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 加密方式配置
 *
 * @Author： hongzhi.xu
 * @Date: 2022/1/27 1:26 下午
 * @Version 1.0
 */

@Data
@ConfigurationProperties(prefix = "verification")
@Component
public class VerificationConfig {
    private String key;
    private String singStr;
}
