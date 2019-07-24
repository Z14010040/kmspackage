package com.sansec.kmspackage.service.impl;

import com.sansec.kmspackage.exception.GlobalException;
import com.sansec.kmspackage.result.CodeMsg;
import com.sansec.kmspackage.result.Result;
import com.sansec.kmspackage.service.PackageService;
import com.sansec.kmspackage.tools.Base64Tools;
import com.sansec.kmspackage.tools.ExecShell;
import com.sansec.kmspackage.tools.LogTool;
import com.sansec.kmspackage.tools.SM2VerifyUtil;
import com.sansec.util.PrintUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.sansec.kmspackage.tools.LogTool.returnErrorInfo;

/**
 * @Author: WeiBingtao/13156050650@163.com
 * @Version: 1.0
 * @Description:
 * @Date: 2019/6/17 18:01
 */
@Service
public class PackageServiceImpl implements PackageService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${kmsPackage.updateSign}")
    String filePath;

    @Override
    public Result packageZipFile(String shell) {
        int result = 0;
        try {
            result = ExecShell.getExecShellProcess(shell).waitFor();
        } catch (InterruptedException e) {
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "",  CodeMsg.PACKAGE_ERROR.getCode(), CodeMsg.PACKAGE_ERROR.getMsg(),
                    null,null));
            return Result.error(CodeMsg.PACKAGE_ERROR.fillArgs(e.getMessage()));
        }
        if(result==0){
            logger.info(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "", CodeMsg.PACKAGE_SUCCESS.getCode(), CodeMsg.PACKAGE_SUCCESS.getMsg(),
                    null,null));
            return Result.success("");
        }else{
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "",  CodeMsg.PACKAGE_ERROR.getCode(), CodeMsg.PACKAGE_ERROR.getMsg(),
                    null,null));
            return Result.error(CodeMsg.PACKAGE_ERROR);
        }
    }

    @Override
    public Result verifySign(){
        //开始验签
        byte[] bs = SM2VerifyUtil.getMD5Two(filePath+"updatefile.zip");
        byte b = (byte) 0x01;
        byte[] bsPad = new byte[32];
        bsPad = Arrays.copyOf(bs, bs.length+16);
        System.out.println("摘要信息（用来和负责签名的人进行比对）：");
        PrintUtil.printWithHex(bsPad);
        Arrays.fill(bsPad, 16, 32, b);

        File signFile = new File(filePath+"updateSign");
        String fileReadResult = SM2VerifyUtil.fileRead2String(signFile);
        byte[] decodeRe = Base64Tools.decode(fileReadResult);
        System.out.println("-----------------签名值长度---------------"+decodeRe.length);
        PrintUtil.printWithHex(decodeRe);

        PublicKey publicKey = null;
        try {
            byte[] keyByte = SM2VerifyUtil.toByteArray("classpath:pubkey_ecc.0");
            publicKey = SM2VerifyUtil.getPublicKey(keyByte);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "",  CodeMsg.PACKAGE_ERROR.getCode(), CodeMsg.PACKAGE_ERROR.getMsg(),
                    null,null));
            throw new GlobalException(CodeMsg.VERIFY_SIGN_ERROR.fillArgs(e.getMessage()));
        }
        boolean verifyResult = false;
        try {
            verifyResult = SM2VerifyUtil.verifySign(publicKey, bsPad, decodeRe);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(LogTool.genLogMsg(MDC.get("ip"),"","", "", "", "",  CodeMsg.PACKAGE_ERROR.getCode(), CodeMsg.PACKAGE_ERROR.getMsg(),
                    null,null));
            throw new GlobalException(CodeMsg.VERIFY_SIGN_ERROR.fillArgs(e.getMessage()));
        }
        if(verifyResult){
            return Result.success("");
        }else{
            throw new GlobalException(CodeMsg.VERIFY_SIGN_ERROR);
        }
    }
}
