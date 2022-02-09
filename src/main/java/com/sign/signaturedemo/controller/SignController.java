package com.sign.signaturedemo.controller;

import com.sign.signaturedemo.common.entity.bo.ResultBO;
import com.sign.signaturedemo.common.entity.dto.DomesticDTO;
import com.sign.signaturedemo.common.entity.dto.SignDTO;
import com.sign.signaturedemo.common.entity.dto.VerificationDTO;
import com.sign.signaturedemo.service.IEncryptionService;
import com.sign.signaturedemo.service.ISignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * @author 刘雪
 * @since 2021/12/24 8:59:07
 */
@RestController
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private ISignService signService;
    @Autowired
    private IEncryptionService iEncryptionService;

    /**
     * 生成签名
     *
     * @param signDTO
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws IOException
     * @throws InvalidKeyException
     */
    @PostMapping("/createSign")
    public String createSign(@RequestBody SignDTO signDTO, HttpServletRequest request) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return signService.createSign(signDTO, request);
    }


    /**
     * 验签
     *
     * @param
     * @return
     */
    @RequestMapping("/checkSign")
    public boolean checkSign(@RequestBody SignDTO signDTO, HttpServletRequest request) throws Exception {
        return signService.checkSign(signDTO, request);
    }


    @PostMapping("/encrypt")
    public String encrypt(@RequestBody SignDTO signDTO, HttpServletRequest request) throws NoSuchAlgorithmException, SignatureException, IOException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return iEncryptionService.encrypt(signDTO, request);
    }

    @PostMapping("decrypt")
    public String decrypt(@RequestBody VerificationDTO verificationDTO, HttpServletRequest request) throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return iEncryptionService.decrypt(verificationDTO);
    }

    @PostMapping("/sm4encrypt")
    public String sm4encrypt(@RequestBody SignDTO signDTO, HttpServletRequest request) throws Exception {
        return iEncryptionService.sm4encrypt(signDTO, request);
    }

    @PostMapping("sm4decrypt")
    public String sm4decrypt(@RequestBody DomesticDTO domesticDTO, HttpServletRequest request) throws Exception {
        return iEncryptionService.sm4decrypt(domesticDTO);
    }
}
