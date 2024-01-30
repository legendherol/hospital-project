package com.ruoyi.hospital.handlerMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.hospital.anno.DecryptedRequestBody;
import com.ruoyi.hospital.domain.CaseResult;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class DecryptHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(DecryptedRequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String json = (String) request.getAttribute("requestData");
        if (json == null) {
            throw new IllegalArgumentException("解密后的请求体为空！");
        }
        // 构造ObjectMapper对象，用于将JSON对象映射到Java对象上
        ObjectMapper objectMapper = new ObjectMapper();
        Object value = objectMapper.readValue(json, CaseResult.class);
        return value;
    }
}