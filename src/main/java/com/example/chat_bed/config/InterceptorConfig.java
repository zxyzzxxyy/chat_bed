package com.example.chat_bed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration//定义此类为配置类
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public HandlerInterceptor UserInterceptor(){
        return new UserInterceptor();
    }

    @Bean
    public HandlerInterceptor ImageInterceptor() {return new ImageInterceptor();}

    @Bean
    public HandlerInterceptor APIInterceptor() {return new APIInterceptor();}


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns拦截的路径
        String[] addPathPatterns = {
                "/user/**"
        };
        //excludePathPatterns排除的路径
        String[] excludePathPatterns = {
                "/user/login",
                "/user/register",
                "/user/verify_phone",
                "/user/gettoken",
                "/user/login_phone",
                "/user/verify_phone_login",
                "/user/changepassword",
                "/user/verify_phone_changepassword",
                "/user/getheself",
                "/user/getheimagelist"
        };
        //创建用户拦截器对象并指定其拦截的路径和排除的路径
        registry.addInterceptor(UserInterceptor()).addPathPatterns(addPathPatterns).excludePathPatterns(excludePathPatterns);
        registry.addInterceptor(ImageInterceptor()).addPathPatterns("/image/**");
        registry.addInterceptor(APIInterceptor()).addPathPatterns("/api/**");
    }
}
