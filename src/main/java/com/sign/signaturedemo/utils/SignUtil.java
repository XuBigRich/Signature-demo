package com.sign.signaturedemo.utils;

import com.sign.signaturedemo.constants.SignConstant;
import okhttp3.HttpUrl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 获取签名的工具类
 * GET - getToken("GET", httpurl, "")
 * POST - getToken("POST", httpurl, json)
 *
 * @author 刘雪
 * @since 2021/12/23 9:26:56
 */
public class SignUtil {

    /**
     * 生成随机数
     *
     * @return
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SignConstant.SYMBOLS.charAt(SignConstant.RANDOM.nextInt(SignConstant.SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 获取token
     *
     * @param method 请求方式
     * @param url 获取请求的绝对URL，并去除域名部分得到参与签名的URL。
     * @param body 请求体
     * @return
     */
    public static String getToken(String method, String url, String body) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException {
        // 先写死 后期根据算法生成  ,生成一个随意字符串 长度32位
        String nonceStr = generateNonceStr();
        //生成一个秒级别的时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        //获取http的url
        HttpUrl httpUrl = HttpUrl.parse(url);
        assert httpUrl != null;
        //构建 要加密的概要信息
        String message = buildMessage(method, httpUrl, timestamp, nonceStr, body);
        // 对概要信息进行签名加密
        String sign = sign(message);
        return "appid=\"" + SignConstant.APP_ID + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "signature=\"" + sign + "\"";
    }

    /**
     * 拼接信息
     *
     * @param method
     * @param url
     * @param timestamp
     * @param nonceStr
     * @param body
     * @return
     */
    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        //对url进行encoded
        String canonicalUrl = url.encodedPath();
        //如果url中参数不为空
        if (url.encodedQuery() != null) {
            //给他将请求拼接到url中
            canonicalUrl += "?" + url.encodedQuery();
        }
        //返回 请求概要信息
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n"
                + SignConstant.SECRET_KEY + "\n";
    }

    /**
     * 获取签名
     *
     * @param message
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     */
    public static String sign(String message) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException {
        Signature signature = Signature.getInstance(SignConstant.SHA256_RSA);
        //初始化 放入私钥
        signature.initSign(getPrivateKey(SignConstant.PRIVATE_KEY));
        //添加要签名信息
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        //   对信息进行签名加密 然后转为 Base64
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    /**
     * 获取私钥。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        try {
            String privateKey = content.replace(SignConstant.BEGIN_PRIVATE_KEY, "").replace(SignConstant.END_PRIVATE_KEY, "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance(SignConstant.RSA);
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * 获取公钥
     *
     * @param fileName 公钥文件路径  (required)
     * @return 公钥对象
     * @throws Exception
     */
    private static PublicKey getPublicKey(String fileName)throws Exception{
        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        String publicKey = content.replace(SignConstant.BEGIN_PUBLIC_KEY, "").replace(SignConstant.END_PUBLIC_KEY, "")
                .replaceAll("\\s+", "");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode(publicKey));
        KeyFactory keyFactory = KeyFactory.getInstance(SignConstant.RSA);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 验签
     *
     * @param signature
     * @param signatureStr
     * @return
     * @throws Exception
     */
    public static boolean responseSignVerify(String signature, String signatureStr) throws Exception {
        Signature signer = Signature.getInstance(SignConstant.SHA256_RSA);
        signer.initVerify(getPublicKey(SignConstant.PUBLIC_KEY));
        signer.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return signer.verify(Base64.getMimeDecoder().decode(signature));
    }

}
