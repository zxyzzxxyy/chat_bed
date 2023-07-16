package com.example.chat_bed.Aop;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
@Mapper
public interface Aoplog {
    void newimagelog(Map map);
    void lookadd(String name);
    void newuserlog(UserLogger log);
    HashMap getusersetting(Integer userid);
    Integer getuserid(UserLogger log);
    String getphone(UserLogger log);
}
