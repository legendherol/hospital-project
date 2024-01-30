package com.ruoyi.hospital.aop;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.hospital.anno.Decrypt;
import com.ruoyi.hospital.anno.Encrypt;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import com.ruoyi.hospital.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

@Slf4j
@Aspect
@Component
public class SafetyAspect {

    private static final String priavateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIhyFhq4/cbH1qbRYrNSkhkzHSiD65EFPfcVk3TZ3isYaGFy/Y4+egkLoX4kwW1VW9z7/MyCKVUdJI0hSuSknKiCmcX3tGnOLZGZqzQBfJ4n134+76OYxRlioEh+BI8Zjx28MMwCDwR9+c1DMF0psF2m5bL14pLt6iiDJYeQXGZZAgMBAAECgYBbRW2R3JMtqDnnBwNuDtrZ7n4fvvcR4B7OLGmh7acWztHr9d60iwhZCqWxWubkuwejMBCvwJXjcIYlvdJ6Vb7QXyjeZJ11K9KRbiRRaCqZg1u3HXPNi3xWHJGtevdjDnoS+ZC5uRKjhsucp180591XeKppru8zznIIKKAfl0JSMQJBAOLHyN0QWXSMwYoOXxUDbGwEfy912pY6j4LpW6xKj3eJ5f5KBNCYnxQgnNZrzSyoM9+sSKMfta5S6C7nAxX+j00CQQCaBqn+JozCk/zUI2akGRa+WHZZi4gXCGgUuipttp+IP+uLK1Iv9ajnib1N2SUuwROPBm8cFI+hlFP2sWdNJsU9AkBRtmbbsI5q/mSmF/OOoMMqUJx7P13Zj5QyOV88v0jea6Ohco41kyiOmgmpAQLWumymhW9Ox5gxDdJ3Q+nKvQRpAkAYNtn8sZTJdjh2JUaan7Mao4+fjjcL3+906ruG2gIEin/+NgZKseUm2VmgZnXY/tOTVaCcGoNmFIqKquMhlunFAkEA4cQQUnyAax3ABpNkRxBaQtXO2ZImudT1r3JoEes7JPu9L1jdPf8V+GU2wb97gr9P1y0e14sDEFIBgJ3mWFH9Hw==";

    /**
     * Pointcut 切入点
     * 匹配
     * cn.huanzi.qch.baseadmin.sys.*.controller、
     * cn.huanzi.qch.baseadmin.*.controller包下面的所有方法
     */
    @Pointcut(value = "execution(public * com.ruoyi.hospital.controller..*.*(..))")
    public void safetyAspect() {}

    /**
     * 环绕通知
     */
    @Around(value = "safetyAspect()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            // request对象
            HttpServletRequest request = attributes.getRequest();
            // http请求方法  post get
            String httpMethod = request.getMethod().toLowerCase();
            // method方法
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();
            // method方法上面的注解
            Annotation[] annotations = method.getAnnotations();
            // 方法的形参参数
            Object[] args = pjp.getArgs();
            // 是否有@Decrypt
            boolean hasDecrypt = false;
            // 是否有@Encrypt
            boolean hasEncrypt = false;
            for (Annotation annotation : annotations) {
                hasDecrypt = annotation.annotationType() == Decrypt.class;
                hasEncrypt = annotation.annotationType() == Encrypt.class;
            }
            ObjectMapper mapper = new ObjectMapper();
            // jackson 序列化和反序列化 date处理
            mapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            // 执行方法之前解密
            if (hasDecrypt) {
                // 此处填坑，后端是用如下的方式获取前端所传参数的，get请求没有问题，但post请求获取body参数时就涉及到request流只能读一次的问题，为了能获取到参数规定前端post请求时使用表单传参，有要求的可自行修改。
                // AES加密后的数据
                String dataTmp = request.getParameter("data");
                log.debug("前端数据：[{}]", dataTmp);
                // 后端RSA公钥加密后的AES的key
                String aesKey = request.getParameter("aesKey");
                log.debug("AES的加密key：[{}]", aesKey);
               //  后端私钥解密的到AES的key
                byte[] plaintext = RSAUtil.decryptByPrivateKey(Base64.decodeBase64(aesKey), priavateKey);
                aesKey = new String(plaintext);
                log.debug("解密出来的AES的key：[{}]", aesKey);
              //   AES解密得到明文data数据

                String decrypt = AESUtil.decrypt(dataTmp, AesKeyUtil.getTansferKey());
                log.debug("解密出来的data数据：[{}]", decrypt);
                // 设置到方法的形参中，目前只能设置只有一个参数的情况
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                if(args.length > 0){
                    args[0] = mapper.readValue(decrypt, args[0].getClass());
                }
            }
            // 执行并替换最新形参参数 method方法要public修饰的才能设置值
            Object o = pjp.proceed(args);

            // 返回结果之前加签名
//            if (hasEncrypt) {
//                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//                // 每次响应之前随机获取AES的key，加密data数据
//                String key = AESUtil.getKey();
//                log.debug("本次响应加密前AES key：[{}]", key);
//                String dataString = mapper.writeValueAsString(o);
//                log.debug("需要加密的data数据：[{}]", dataString);
//                String data = AESUtil.encrypt(dataString, key);
//                log.debug("加密后的data数据：[{}]", data);
//                // 对key 用私钥加签
//                String sign = RSAUtil.sign(key.getBytes(), RSAUtil.getPrivateKey());
//                // 转json字符串并转成Object对象，并赋值给返回值o
//                o = AjaxResult.success(mapper.readValue(String.format("{\"data\":\"%s\",\"aesKey\":\"%s\",\"sign\":\"%s\"}", data, key, sign), Object.class));
//            }
            return o;
        } catch (Throwable e) {
            log.error(e.getMessage());
            // CommonResult为后端统一返回类
            return AjaxResult.error(HttpStatus.ERROR, e.getMessage());
        }
    }
}
