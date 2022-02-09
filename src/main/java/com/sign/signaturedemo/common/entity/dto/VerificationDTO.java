package com.sign.signaturedemo.common.entity.dto;

import lombok.Data;

/**
 * 加密请求体
 * @Author： hongzhi.xu
 * @Date: 2022/1/27 2:15 下午
 * @Version 1.0
 */
@Data
public class VerificationDTO {
    private String t;
    private String agencyId;
    private String data;
    private String signature;
}
