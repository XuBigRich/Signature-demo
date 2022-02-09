package com.sign.signaturedemo.service;

import com.sign.signaturedemo.common.entity.dto.DomesticDTO;
import com.sign.signaturedemo.common.entity.dto.SignDTO;
import com.sign.signaturedemo.common.entity.dto.VerificationDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author： hongzhi.xu
 * @Date: 2022/1/27 3:22 下午
 * @Version 1.0
 */

public interface IEncryptionService {
    String encrypt(SignDTO signDTO, HttpServletRequest request) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    String decrypt(VerificationDTO verificationDTO) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException;

    String sm4encrypt(SignDTO signDTO, HttpServletRequest request) throws Exception;

    String sm4decrypt(DomesticDTO verificationDTO) throws Exception;
}
