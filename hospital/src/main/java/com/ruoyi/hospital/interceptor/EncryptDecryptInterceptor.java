//package com.ruoyi.hospital.interceptor;
//
//import com.ruoyi.hospital.anno.EncryptInter;
//import com.ruoyi.hospital.util.AESUtil;
//import com.ruoyi.hospital.util.AesKeyUtil;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.sql.Statement;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//@Intercepts({
//        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
//        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})
//})
//public class EncryptDecryptInterceptor implements Interceptor {
//
//    private static String aeskey = null;
//
//    static {
//        try {
//            aeskey = AesKeyUtil.getEncryptKey();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public Object intercept(Invocation invocation) throws Throwable {
//        // If the parameter object is a map
//        Object[] args = invocation.getArgs();
//        // If the parameter object is not a map but an object
//        if (args != null && args.length > 0 && args[0] instanceof Object) {
//            Object param = args[0];
//            Class<?> clazz = param.getClass();
//            Field[] fields = clazz.getDeclaredFields();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                Object value = field.get(param);
//                if (value != null && field.isAnnotationPresent(EncryptInter.class) &&
//                        value instanceof String) {
//                    // EncryptInter the field value
//                    EncryptInter encrypt = field.getAnnotation(EncryptInter.class);
//                    if (encrypt != null) {
//                        String encryptedValue = AESUtil.encrypt((String) value,aeskey);
//                        field.set(param, encryptedValue);
//                    }
//                }
//            }
//        }
//
//        Object result = invocation.proceed();
//        if (result!=null) {
//            // If the result is a list of objects
//            if (result instanceof List<?>) {
//                List<?> resultList = (List<?>) result;
//                for (Object item : resultList) {
//                    if (item != null) {
//                        Class<?> clazz = item.getClass();
//                        Field[] fields = clazz.getDeclaredFields();
//                        for (Field field : fields) {
//                            if (field.isAnnotationPresent(EncryptInter.class) &&
//                                    field.getType().equals(String.class)) {
//                                // Decrypt the field value
//                                field.setAccessible(true);
//                                String encryptedText = (String) field.get(item);
//                                if (encryptedText != null) {
//                                    String decryptedText = AESUtil.decrypt(encryptedText, aeskey);
//                                    field.set(item, decryptedText);
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                decryptObjectFields(result, aeskey);
//            }
//        }
//
//
//        return result;
//
//    }
//
//    @Override
//    public Object plugin(Object target) {
//        return Plugin.wrap(target, this);
//    }
//
//    @Override
//    public void setProperties(Properties properties) {
//        // do nothing
//    }
//
//    public static void decryptObjectFields(Object obj, String aeskey) throws Exception {
//        Class<?> clazz = obj.getClass();
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            if (field.isAnnotationPresent(EncryptInter.class) && field.getType().equals(String.class)) {
//                // Decrypt the field value
//                field.setAccessible(true);
//                String encryptedText = (String) field.get(obj);
//                if (encryptedText != null) {
//                    String decryptedText = AESUtil.decrypt(encryptedText, aeskey);
//                    field.set(obj, decryptedText);
//                }
//            }
//        }
//    }
//}