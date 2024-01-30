package com.ruoyi.hospital.config;

import com.ruoyi.hospital.interceptor.CryptoInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MybatisConfig {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void config(){
        sqlSessionFactory.getConfiguration().addInterceptor(new CryptoInterceptor());
    }
}
