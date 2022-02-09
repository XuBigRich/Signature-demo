package com.sign.signaturedemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sign.signaturedemo.common.entity.bo.ResultBO;
import com.sign.signaturedemo.common.enums.ResultEnum;
import com.sign.signaturedemo.constants.SignConstant;
import com.sign.signaturedemo.common.entity.dto.SignDTO;
import com.sign.signaturedemo.service.ISignService;
import com.sign.signaturedemo.utils.HttpUtil;
import com.sign.signaturedemo.utils.SignUtil;
import okhttp3.HttpUrl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现层
 *
 * @author 刘雪
 * @since 2021/12/27 9:09:27
 */
@Service
public class SignServiceImpl implements ISignService {

    @Override
    public String createSign(SignDTO signDTO, HttpServletRequest request) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException {
        String token = "";
        String method = request.getMethod();
        if (SignConstant.GET.equals(method)) {
            //如果是get请求 ，请求参数在url中
            token = SignUtil.getToken(method, signDTO.getUrl(), "");
        } else {
            //如果是post请求，将请求体中数据去处放入，
            token = SignUtil.getToken(method, signDTO.getUrl(), JSONObject.toJSONString(signDTO.getBody()));
        }
        //给token添加一个前缀
        String message = SignConstant.SCHEMA + " " + token;
        // 拼接请求头
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", message);
        // 发送请求
        if (SignConstant.GET.equals(method)) {
            //将请求发送出去
            HttpUtil.sendGet(signDTO.getUrl(), headerMap);
        } else {
            Map map = JSON.parseObject(JSON.toJSONString(signDTO.getBody()), Map.class);
            //将请求发送出去
            HttpUtil.sendPost(signDTO.getUrl(), map, headerMap);
        }
        return message;
    }

    @Override
    public boolean checkSign(SignDTO signDTO, HttpServletRequest request) throws Exception {
        //获取请求头
        String authorization = request.getHeader("Authorization");
        // 拆分
        String token = authorization.substring(authorization.indexOf(" "));
        token = token.replaceAll("=", ":");
        token = "{" + token + "}";
        JSONObject jsonObject = JSONObject.parseObject(token);
        HttpUrl parse = HttpUrl.parse(signDTO.getUrl());
        assert parse != null;
        String message = "";
        // 判断请求方式
        if (SignConstant.POST.equals(request.getMethod())) {
            message = SignUtil.buildMessage(request.getMethod(), parse, jsonObject.getLong(SignConstant.TIMESTAMP), jsonObject.getString(SignConstant.NONCE_STR),
                    JSONObject.toJSONString(signDTO.getBody()));
        } else {
            message = SignUtil.buildMessage(request.getMethod(), parse, jsonObject.getLong(SignConstant.TIMESTAMP), jsonObject.getString(SignConstant.NONCE_STR),
                    "");
        }
        return SignUtil.responseSignVerify(jsonObject.getString(SignConstant.SIGNATURE), message);
    }

}
