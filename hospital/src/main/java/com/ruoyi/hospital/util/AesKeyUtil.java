package com.ruoyi.hospital.util;

import org.bouncycastle.jcajce.provider.symmetric.AES;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AesKeyUtil {

    public static String getEncryptKey() throws IOException {
        String filename = AesKeyUtil.class.getClassLoader().getResource("aes/aes_encrypt").getFile();
        try {
            byte[] bytes = readFile(filename);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private static byte[] readFile(String fileName) throws Exception {
        return Files.readAllBytes(new File(fileName).toPath());
    }

    public static String getTansferKey() throws IOException {
        String filename = AesKeyUtil.class.getClassLoader().getResource("aes/aes_transfer").getFile();
        try {
            byte[] bytes = readFile(filename);
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) throws IOException {
        System.out.println(getEncryptKey());
        System.out.println(getTansferKey());
    }

}
