package com.sign.signaturedemo.common.entity.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 刘雪
 * @since 2021/12/24 9:20:43
 */
@Data
@Accessors(chain = true)
public class SignDTO {

    private String method;

    private String url;

    private JSONObject body;

}
