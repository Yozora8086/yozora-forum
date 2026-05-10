package com.lcy.yozoraforum.config;

import com.lcy.yozoraforum.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 配置类
 */

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {
    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //注册jwt令牌拦截器
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/yozora/**")
                .excludePathPatterns("/yozora/user/login")
                .excludePathPatterns("/yozora/user/register")
                .excludePathPatterns("/yozora/forum/showList")
                .excludePathPatterns("/yozora/forum/getAllForum");

    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径
                .allowedOrigins("http://localhost:5173") // 允许前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
                .allowCredentials(true);
    }
}
