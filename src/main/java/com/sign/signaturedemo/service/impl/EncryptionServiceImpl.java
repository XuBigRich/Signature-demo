package com.sign.signaturedemo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sign.signaturedemo.common.entity.dto.DomesticDTO;
import com.sign.signaturedemo.common.entity.dto.SignDTO;
import com.sign.signaturedemo.common.entity.dto.VerificationDTO;
import com.sign.signaturedemo.service.IEncryptionService;
import com.sign.signaturedemo.utils.HttpUtil;
import com.sign.signaturedemo.utils.Sm4Util;
import com.sign.signaturedemo.utils.VerificationSignUtil;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author： hongzhi.xu
 * @Date: 2022/1/27 1:23 下午
 * @Version 1.0
 */
@Service
public class EncryptionServiceImpl implements IEncryptionService {


    @Override
    public String encrypt(SignDTO signDTO, HttpServletRequest request) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        JSONObject body = signDTO.getBody();
        //获得原请求
        String content = body.toJSONString();
        //将请求参数加密
        String decryptContent = VerificationSignUtil.encrypt(content);
        VerificationDTO verificationDTO = new VerificationDTO();
        String timestamp = System.currentTimeMillis() + "";
        verificationDTO.setT(timestamp);
        //乱造一个商户号
        verificationDTO.setAgencyId("123123");
        verificationDTO.setData(decryptContent);
        //对加密后的数据（data密文）拼接13位时间戳 得到待签名字符串singStr 然后 签名方法的到 signContent
        String signContent = VerificationSignUtil.sign(decryptContent + timestamp);
        verificationDTO.setSignature(signContent);
        //将请求发送出去
        JSONObject map = (JSONObject) JSONObject.toJSON(verificationDTO);
        System.out.println(map);
        HttpUtil.sendPost(signDTO.getUrl(), map, new HashMap<>());
        return verificationDTO.toString();
    }

    @Override
    public String decrypt(VerificationDTO verificationDTO) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        String signature = verificationDTO.getSignature();
        String data = verificationDTO.getData();
        String t = verificationDTO.getT();
        //查看加密信息是否被篡改
        String checkSign = VerificationSignUtil.sign(data + t);
        if (!checkSign.equals(signature)) {
            return "签名验证失败";
        }
        //解谜信息
        String decryptContent = VerificationSignUtil.decrypt(data);
        System.out.println(decryptContent);
        return decryptContent;
    }

    @Override
    public String sm4encrypt(SignDTO signDTO, HttpServletRequest request) throws Exception {
        JSONObject body = signDTO.getBody();
        //获得原请求
        String content = body.toJSONString();
        //将请求参数加密
        String decryptContent = Sm4Util.encrypt(content.getBytes(StandardCharsets.UTF_8));
        VerificationDTO verificationDTO = new VerificationDTO();
        String timestamp = System.currentTimeMillis() + "";
        verificationDTO.setT(timestamp);
        //乱造一个商户号
        verificationDTO.setAgencyId("123123");
        verificationDTO.setData(decryptContent);
        //对加密后的数据（data密文）拼接13位时间戳 得到待签名字符串singStr 然后 签名方法的到 signContent
        String signContent = Sm4Util.sign(decryptContent);
        verificationDTO.setSignature(signContent);
        //将请求发送出去
        JSONObject map = (JSONObject) JSONObject.toJSON(verificationDTO);
        System.out.println(map);
        HttpUtil.sendPost(signDTO.getUrl(), map, new HashMap<>());
        return verificationDTO.toString();
    }

    @Override
    public String sm4decrypt(DomesticDTO verificationDTO) throws Exception {
        String signature = verificationDTO.getSignature();
        String data = verificationDTO.getEncrypt();
        //查看加密信息是否被篡改
        String checkSign = Sm4Util.sign(data);
        if (!checkSign.equals(signature)) {
            return "签名验证失败";
        }
        //解密信息
        String decryptContent = Sm4Util.decrypt(data);
        System.out.println(decryptContent);
        return decryptContent;
    }
}
