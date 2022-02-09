package com.sign.signaturedemo.utils;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * 国密SM4分组密码算法工具类（对称加密）
 * <p>GB/T 32907-2016 信息安全技术 SM4分组密码算法</p>
 * <p>SM4-ECB-PKCS5Padding</p>
 */
public class Sm4Util {

    private static final String ALGORITHM_NAME = "SM4/CBC/PKCS7Padding";
    //对称加密的加解密密钥
    private static final String SM4_KEY = "JeF8U9wHFOMfs2Y8";
    //加密向量
    private static final String IV = "JeF8U9wHFOMfs2Y8";

    /**
     * SM4算法目前只支持128位（即密钥16字节）
     */
    private static final int DEFAULT_KEY_SIZE = 128;

    static {
        // 防止内存中出现多次BouncyCastleProvider的实例
        if (null == Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private Sm4Util() {
    }

    /**
     * 生成密钥
     * <p>建议将二进制转成HEX</p>
     *
     * @return 密钥16位
     * @throws Exception 生成密钥异常
     */
    public static byte[] generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(DEFAULT_KEY_SIZE, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * 加密，SM4-ECB-PKCS5Padding
     *
     * @param data 要加密的明文
     * @return 加密后的密文
     * @throws Exception 加密异常
     */
    public static String encrypt(byte[] data) throws Exception {
        byte[] key = SM4_KEY.getBytes(StandardCharsets.UTF_8);
        return sm4(data, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密，SM4-ECB-PKCS5Padding
     *
     * @param data 要解密的密文
     * @return 解密后的明文
     * @throws Exception 解密异常
     */
    public static String decrypt(String data) throws Exception {
        byte[] byteContent = Base64.getDecoder().decode(data);
        byte[] key = SM4_KEY.getBytes(StandardCharsets.UTF_8);
        return sm4(byteContent, key, Cipher.DECRYPT_MODE);
    }


    /**
     * SM4对称加解密
     *
     * @param input 明文（加密模式）或密文（解密模式）
     * @param key   密钥
     * @param mode  Cipher.ENCRYPT_MODE - 加密；Cipher.DECRYPT_MODE - 解密
     * @return 密文（加密模式）或明文（解密模式）
     * @throws Exception 加解密异常
     */
    private static String sm4(byte[] input, byte[] key, int mode)
            throws Exception {
        SecretKeySpec sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        Cipher cipher = Cipher
                .getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        cipher.init(mode, sm4Key, ivParameterSpec);
        byte[] data = cipher.doFinal(input);
        if (mode == Cipher.ENCRYPT_MODE) {
            return Base64.getEncoder().encodeToString(data);
        }
        return new String(data);
    }

    /**
     * 传入必须是偶数
     *
     * @param sm4Key
     * @return
     */
    private static byte[] hexToBin(String sm4Key) {
        return Hex.decode(sm4Key);
    }

    //验签名
    public static String sign(String data) {
        byte[] srcData = data.getBytes(StandardCharsets.UTF_8);
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return byteArrayToHexString(hash);
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

