package com.netlab.loveofmum.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import com.netlab.loveofmum.utils.decode.Base64;

/**
 * Created by Administrator on 2016/4/18.
 */
public class Des {
    private final static String secretKey = "liuyunqiang@lx100$#365#$" ;
    // 向量
    private final static String iv = "01234567" ;
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8" ;

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte [] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }
    /**
     * 加密和解密
     *
     * @param args
     */
    public static void main(String[] args) {
        /** 数据初始化 **/
        String content = "12346469";

        /** 加密 **/
        try {
            System.out.println("加密前：" + content);
            String encryptResultStr = encode(content);
            System.out.println("加密后：" + encryptResultStr);
            /** 解密 **/
            String decryptString = decode(encryptResultStr);
            System.out.println("解密后：" + decryptString);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
