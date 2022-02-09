package com.sign.signaturedemo.service;

import com.sign.signaturedemo.common.entity.dto.SignDTO;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * 服务层
 *
 * @author 刘雪
 * @since 2021/12/27 9:09:00
 */
public interface ISignService {

    String createSign(SignDTO signDTO, HttpServletRequest request) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;

    boolean checkSign(SignDTO signDTO, HttpServletRequest request) throws Exception;

}
