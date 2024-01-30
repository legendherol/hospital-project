package com.ruoyi.hospital.interceptor;

import com.ruoyi.hospital.anno.EncryptInter;
import com.ruoyi.hospital.util.AESUtil;
import com.ruoyi.hospital.util.AesKeyUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class CryptoInterceptor implements Interceptor {
    private static String AES_KEY = null;

    static {
        try {
            AES_KEY = AesKeyUtil.getEncryptKey();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        // 加密 - 处理插入和更新操作
        if ("update".equals(getMethodName(invocation))) {
            try {
                encryptSensitiveField(parameterObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error encrypting field.", e);
            }
        }

        // 执行原始方法
        Object result = invocation.proceed();

        // 解密 - 处理查询结果
        if ("query".equals(getMethodName(invocation))) {
            if (result instanceof List) {
                List<?> listResult = (List<?>) result;
                for (Object obj : listResult) {
                    try {
                        decryptSensitiveField(obj);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error in decrypting field.", e);
                    }
                }
            } else {
                try {
                    decryptSensitiveField(result);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error in decrypting field.", e);
                }
            }
        }

        return result;
    }



    private String getMethodName(Invocation invocation) {
        return invocation.getMethod().getName();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }


    private void encryptSensitiveField(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EncryptInter.class)) {
                field.setAccessible(true);
                String value = (String) field.get(obj);
                String encryptedValue = AESUtil.encrypt(value, AES_KEY);
                field.set(obj, encryptedValue);
            }
        }
    }

    private void decryptSensitiveField(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EncryptInter.class)) {
                field.setAccessible(true);
                String encryptedValue = (String) field.get(obj);
                String decryptedValue = AESUtil.decrypt(encryptedValue, AES_KEY);
                field.set(obj, decryptedValue);
            }
        }
    }

}