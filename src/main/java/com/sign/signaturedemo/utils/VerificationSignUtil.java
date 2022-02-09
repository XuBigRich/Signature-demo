package com.sign.signaturedemo.utils;

import com.sign.signaturedemo.config.VerificationConfig;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author： hongzhi.xu
 * @Date: 2022/1/27 1:24 下午
 * @Version 1.0
 */
public class VerificationSignUtil {
    //加密
    public static String encrypt(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
        byte[] byteContent = content.getBytes("utf-8");
        SecretKeySpec keySpec = new SecretKeySpec(SpringUtils.getBean(VerificationConfig.class).getKey().getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化为加密模式的密码器
        byte[] result = cipher.doFinal(byteContent);// 加密
        return Base64.getEncoder().encodeToString(result);
    }

    //解密
    public static String decrypt(String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// 创建密码器
        byte[] byteContent = Base64.getDecoder().decode(content);
        SecretKeySpec keySpec = new SecretKeySpec(SpringUtils.getBean(VerificationConfig.class).getKey().getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);// 初始化为加密模式的密码器
        byte[] result = cipher.doFinal(byteContent);//
        return new String(result);
    }

    //验签名
    public static String sign(String content) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(SpringUtils.getBean(VerificationConfig.class).getSingStr().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(spec);
        byte[] result = mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
        //将二进制字符转为 十六进制
        return byteArrayToHexString(result);
    }

    //将二进制字符转为 十六进制
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        String stmp;
        for (int i = 0; b != null && i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0");
            }
            sb.append(stmp);
        }
        return sb.toString().toLowerCase();
    }
}
