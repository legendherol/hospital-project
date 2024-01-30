package com.ruoyi.common.utils.sign;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Md5加密方法
 *
 * @author ruoyi
 */
public class Md5Utils {
    private static final Logger log = LoggerFactory.getLogger(Md5Utils.class);

    private static byte[] md5(String s) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(s.getBytes("UTF-8"));
            byte[] messageDigest = algorithm.digest();
            return messageDigest;
        } catch (Exception e) {
            log.error("MD5 Error...", e);
        }
        return null;
    }

    private static final String toHex(byte hash[]) {
        if (hash == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(hash.length * 2);
        int i;

        for (i = 0; i < hash.length; i++) {
            if ((hash[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(hash[i] & 0xff, 16));
        }
        return buf.toString();
    }

    public static String hash(String s) {
        try {
            return new String(toHex(md5(s)).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("not supported charset...{}", e);
            return s;
        }
    }

    /**
     * 使用 MessageDigest 进行 md5 加密
     *
     * @param data 待加密数据
     * @return 加密后的字符串
     */
    public static String getMD5(String data) {
        try {
            // 获取加密器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 将字符串转换为字节数组并进行加密
            byte[] byteArray = md5.digest(data.getBytes());
            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                String hex = Integer.toHexString(0xFF & byteArray[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static void main(String[] args) {
        String a = "123456";
        String s1 = JSONObject.toJSONString(a);
        String bytes = getMD5(s1);
        System.out.println(bytes);

        System.out.println(bytes.equals("299f39bf4e1d6d2328717ae49f016172"));
    }
}
