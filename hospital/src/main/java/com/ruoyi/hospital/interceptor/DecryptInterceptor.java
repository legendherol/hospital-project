package com.ruoyi.hospital.interceptor;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.hospital.anno.DecryptInterAnno;
import com.ruoyi.hospital.anno.DecryptedRequestBody;
import com.ruoyi.hospital.domain.Doctor;
import com.ruoyi.hospital.domain.Patient;
import com.ruoyi.hospital.domain.UserRsaKey;
import com.ruoyi.hospital.mapper.DoctorMapper;
import com.ruoyi.hospital.mapper.PatientMapper;
import com.ruoyi.hospital.mapper.UserRsaKeyMapper;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import com.ruoyi.hospital.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class DecryptInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            // 在参数注解上进行检查
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof DecryptedRequestBody) {
                        // 获取String类型的请求体
                        InputStream inputStream = request.getInputStream();
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) != -1) {
                            result.write(buffer, 0 , length);
                        }
//                        byte[] byteArrayResult = result.toByteArray();
//                        String s = Base64.encodeBase64String(byteArrayResult);
                        String requestData = result.toString("UTF-8");

                        if (!StringUtils.hasText(requestData)) {
                            // 如果请求体为空，直接返回
                            return true;
                        }
                        try {
                            // 解密请求参数
                            String decryptedData = decryptData(requestData, request);
                            // 将解密后的参数设置回请求流中
                            request.setAttribute("requestData", decryptedData);
                        } catch (Exception e) {
                            // 解密失败，返回错误
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "请求参数解密失败！");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private String getRequestData(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private String decryptData(String encryptedData,HttpServletRequest request) throws Exception {
        // 使用与前端相同的方式解密数据
        String decrypt = AESUtil.decrypt(encryptedData, AesKeyUtil.getTansferKey());
        String[] dataArray = decrypt.split("\\|");
        // rsa私钥加密后md5摘要
        String rsaEnMd5Value = dataArray[0];
        // 源数据
        String original = dataArray[1];
        // 元数据md5加密后摘要
        String md5 = Md5Utils.getMD5(original);
        SysUser user = SecurityUtils.getLoginUser().getUser();
        String phonenumber = user.getPhonenumber();
        // 获取Spring上下文对象
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        // 通过Spring上下文获取Mapper对象
        DoctorMapper doctorMapper = context.getBean(DoctorMapper.class);
        UserRsaKeyMapper userRsaKeyMapper = context.getBean(UserRsaKeyMapper.class);
        String encrypt = AESUtil.encrypt(phonenumber, AesKeyUtil.getEncryptKey());
        Doctor doctor = doctorMapper.findDocByPhone(encrypt);
        String userId = doctor.getUserId();
        // 获取rsa密钥对
        UserRsaKey userRsaKey = userRsaKeyMapper.selectUserRsaKeyByUserId(userId);
        String userPrivateKey = userRsaKey.getUserPrivateKey();
        String userPublicKey = userRsaKey.getUserPublicKey();

        // 解密和公钥
        byte[] bytes = RSAUtil.decryptByPublicKey(Base64.decodeBase64(rsaEnMd5Value.getBytes()), userPublicKey);
        if (md5.equals(new String(bytes, StandardCharsets.UTF_8))){
            throw new ServiceException("数据完整性校验不通过或数字签名校验不通过！");
        }

        return original;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // empty implementation
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // empty implementation
    }
}