package com.netlab.loveofmum.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ThreeDes {
    public static final byte[] Key = "abcdefgh".getBytes();
    private static final String Algorithm = "DES";  //定义 加密算法,可用 DES,DESede,Blowfish
 
    // 加密字符串
    public static byte[] encryptMode(byte[] src) {
        try { // 生成密钥
            SecretKey deskey = new SecretKeySpec(Key, Algorithm); // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
 
    // 解密字符串
    public static byte[] decryptMode(byte[] src) {
        try { // 生成密钥
            SecretKey deskey = new SecretKeySpec(Key, Algorithm); // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
 
    public static void main(String[] args) { // 添加新安全算法,如果用JCE就要把它添加进去
        final byte[] keyBytes = Key;    //8字节的密钥
        String szSrc = "wuwei";
        System.out.println("加密前的字符串:" + szSrc);
        byte[] encoded = encryptMode(szSrc.getBytes());
        System.out.println("加密后的字符串:" + new String(encoded));
        byte[] srcBytes = decryptMode(encoded);
        System.out.println("解密后的字符串:" + (new String(srcBytes)));
    }
}
