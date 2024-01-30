package com.ruoyi.hospital.util;

import com.ruoyi.hospital.domain.Patient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class RSAUtil {



    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 算法名称/加密模式/数据填充方式
     * 默认：RSA/ECB/PKCS1Padding
     */
    private static final String ALGORITHMS = "RSA/ECB/PKCS1Padding";

    /**
     * Map获取公钥的key
     */
    private static final String PUBLIC_KEY = "publicKey";

    /**
     * Map获取私钥的key
     */
    private static final String PRIVATE_KEY = "privateKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 1024 117 128
     * RSA 位数 如果采用2048 上面最大加密和最大解密则须填写:  245 256
     */
    private static final int INITIALIZE_LENGTH = 1024;

    /**
     * 后端RSA的密钥对(公钥和私钥)Map，由静态代码块赋值
     */
    private static Map<String, Object> genKeyPair = new LinkedHashMap<>();

    static {
        try {
            genKeyPair.putAll(genKeyPair());
        } catch (Exception e) {
            // 输出到日志文件中
            log.error(e.getMessage());
            // System.err.println(e.getMessage());
        }
    }

    /**
     * 生成密钥对(公钥和私钥)
     */
    private static Map<String, Object> genKeyPair() throws Exception {
        log.info("-------------------开始生成密钥对");
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(INITIALIZE_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<>(2);
        //公钥
        keyMap.put(PUBLIC_KEY, publicKey);
        //私钥
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 私钥解密
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) throws Exception {
        //base64格式的key字符串转Key对象
        Key privateK = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        //分段进行解密操作
        return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_DECRYPT_BLOCK);
    }

    /**
     * 公钥加密
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        //base64格式的key字符串转Key对象
        Key publicK = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        //分段进行加密操作
        return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 私钥加密
     * @param data       源数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        Key privateK = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        return encryptAndDecryptOfSubsection(data, cipher, MAX_ENCRYPT_BLOCK);
    }

    /**
     * 公钥解密
     * @param encryptedData 已加密数据
     * @param publicKey     公钥(BASE64编码)
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        Key publicK = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey)));
        Cipher cipher = Cipher.getInstance(ALGORITHMS);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return encryptAndDecryptOfSubsection(encryptedData, cipher, MAX_ENCRYPT_BLOCK);

    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey() {
        Key key = (Key) genKeyPair.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey() {
        Key key = (Key) genKeyPair.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     * 分段进行加密、解密操作
     */
    private static byte[] encryptAndDecryptOfSubsection(byte[] data, Cipher cipher, int encryptBlock) throws Exception {
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > encryptBlock) {
                cache = cipher.doFinal(data, offSet, encryptBlock);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * encryptBlock;
        }
        out.close();
        return out.toByteArray();
    }

    /**
     * 用私钥对信息生成数字签名
     * @param data       已加密数据
     * @param privateKey 私钥(BASE64编码)
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(privateKey);
        PrivateKey privateK = KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data      已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign      数字签名
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        PublicKey publicK = KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(keyBytes));
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decodeBase64(sign));
    }

//    // 加密方法
//    public String encryptPatientData(String data,String publicKey) throws Exception {
//        // 对数据进行序列化，并进行 Base64 编码
//        byte[] bytes = Base64.encodeBase64(data.getBytes());
//        // 使用公钥加密数据
//        byte[] encryptedData = encryptByPublicKey(bytes, publicKey);
//        // 对加密后的数据进行 Base64 编码，并转换成字符串
//        return Base64.getEncoder().encodeToString(encryptedData);
//    }
//
//    // 解密方法
//    public String decryptPatientData(String encryptedData,String privateKey) throws Exception {
//        // 先将数据进行 Base64 解码
//        byte[] base64Data = Base64.getDecoder().decode(encryptedData);
//        // 使用私钥解密数据
//        byte[] decryptedData = decryptByPrivateKey(base64Data, privateKey);
//        // 对解密后的数据进行 Base64 解码，并进行反序列化
//        return (Patient) ByteUtil.byteToObject(Base64.getDecoder().decode(decryptedData));
//    }

    public static void main(String[] args) throws Exception {

        String encryptKey = AesKeyUtil.getEncryptKey();
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCE5q4jyKr9VNgwFLuEHa0H8WZLyVKp+MFHiomrT24lm1KiD1H478iM0uiu8Y1NyMjoufqzHDhvN1Gp2KBIpUQMQrug7nw+Yy9BgS/IRmEUnwANx2yjajr91ua1N9amn2rDdyYZt7hgvIfDKL4ms/hkcxeSFdNlE7poBkvd2EkfzwIDAQAB";
        System.out.println(publicKey);
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAITmriPIqv1U2DAUu4QdrQfxZkvJUqn4wUeKiatPbiWbUqIPUfjvyIzS6K7xjU3IyOi5+rMcOG83UanYoEilRAxCu6DufD5jL0GBL8hGYRSfAA3HbKNqOv3W5rU31qafasN3Jhm3uGC8h8Moviaz+GRzF5IV02UTumgGS93YSR/PAgMBAAECgYBUvbIz5vQAbGUc41tdtn5qHiaYFFq9PnwfXdrCfPdCy3M5GoOuM1axVJpBUoxDGM/idRPBuTDS2Xcc0aACMj7ckHdxB7Zy+7b7jptGxyJxeboPDAj1J8qW9afw5Mefqtt2NQck6UaNW1947z3e86G1/hHBItrLpSN3vLBzhD0TQQJBAOp+Y/ARa21V8feH6886GJ4KmiMOVTe3mNb98LzwtWRsWSaSJ6WwPqR01i9zn5Bkl/47h6lcjXdutUw4hEIy90kCQQCRFwa+54lC+jmSYvIWmLTqLB3peLCN6smKao2PjW3FuE0Nn3mzeL2KKZcw3/4mGOXegCc2gWNvNmw53buG8GZXAkEA59l7unueugYGLfYoA0av3GMjL5IWU/almbyO8GEXGGnAY9CSftIZvcT/mP87PysW1PnLEns+LF47x1cB/eoicQJAHPxxr5AIPyZIfDNiVBtcAXCg5TTkaOXgL9QLxgMmTBr+4drbcHyNuV2HxA7r8maE/geyTdRZri08P+9JeLLsbwJAblvbc4ckq2HzbboFhPmn0fqGuQF7eV2KhCZBWXkFvbPKFg+nKMGadLM1qQu7e2bGVbdnYfNcg4eLHV8UEItp5A==";
        byte[] bytes = encryptByPublicKey(encryptKey.getBytes(), publicKey);
        String s = Base64.encodeBase64String(bytes);
        System.out.println(s);
//        System.out.println(new String(bytes));
        byte[] bytes2 = Base64.decodeBase64(s);
        byte[] bytes1 = decryptByPrivateKey(bytes2, privateKey);
        System.out.println(new String(bytes1));
//        System.out.println(publicKey);
//        Patient patient = new Patient();
//        patient.setAge(15L);
//        patient.setPatientName("阿松大ljl");
        // 使用 Base64 进行序列化
//        byte[] bytes2 = Base64.encodeBase64(ByteUtil.objectToByte(patient));
//        byte[] bytes = encryptByPublicKey(bytes2,publicKey);

        // // 使用 Base64 进行反序列化
//        byte[] bytes3 = Base64.decodeBase64(bytes);
//        Patient patient2 = (Patient) ByteUtil.byteToObject(Base64.decodeBase64(bytes));
//        System.out.println("加密后对象get:"+patient2.getPatientName());
//
//        System.out.println("加密后对象:" + new String(bytes));
//        byte[] bytes1 = decryptByPrivateKey(bytes, privateKey);
//        byte[] bytes3 = Base64.decodeBase64(bytes1);
//        Patient patient1 = (Patient)ByteUtil.byteToObject(bytes3);
//
//        System.out.println("解密后数据:名字是:"+patient1.getPatientName());

    }

}
