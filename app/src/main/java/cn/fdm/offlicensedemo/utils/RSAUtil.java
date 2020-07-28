package cn.fdm.offlicensedemo.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtil {

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException 生成密钥对过程中的异常信息
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        //得到公钥字符串
        System.out.println("公钥：" + Base64.encodeToString(publicKey.getEncoded(), Base64.NO_WRAP));
        // 得到私钥字符串
        System.out.println("私钥：" + Base64.encodeToString(privateKey.getEncoded(), Base64.NO_WRAP));
    }

    /**
     * RSA签名
     *
     * @param priKeyStr RSA私钥
     * @param srcStr    待签名数据
     * @return 签名值
     * @throws Exception 签名过程中的异常信息
     */
    public static String sign(String priKeyStr, String srcStr) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode(priKeyStr, Base64.NO_WRAP));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initSign(priKey);
        signature.update(srcStr.getBytes(StandardCharsets.UTF_8));
        byte[] signed = signature.sign();
        return Base64.encodeToString(signed, Base64.NO_WRAP);
    }

    /**
     * RSA-SHA1公钥验签
     *
     * @param publicKeyStr RSA公钥字符串
     * @param srcStr       原文字符串
     * @param signStr      签名字符串
     * @return true.有效的签名，false.无效的签名
     * @throws Exception 验签过程中的异常信息
     */
    public static boolean verifySign(String publicKeyStr, String srcStr, String signStr) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decode(publicKeyStr, Base64.NO_WRAP);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        Signature signature = Signature.getInstance("SHA1WithRSA");
        signature.initVerify(pubKey);
        signature.update(srcStr.getBytes(StandardCharsets.UTF_8));
        return signature.verify(Base64.decode(signStr, Base64.NO_WRAP));
    }

    /**
     * RSA公钥加密
     *
     * @param clientPubKey RSA公钥
     * @param str          加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String clientPubKey, String str) throws Exception {
        byte[] decoded = Base64.decode(clientPubKey, Base64.NO_WRAP);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeToString(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)), Base64.NO_WRAP);
    }

    /**
     * RSA私钥解密
     *
     * @param str              加密字符串
     * @param clientPrivateKey RSA私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String clientPrivateKey) throws Exception {
        byte[] inputByte = Base64.decode(str, Base64.NO_WRAP);
        byte[] decoded = Base64.decode(clientPrivateKey, Base64.NO_WRAP);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte), StandardCharsets.UTF_8);
    }

}