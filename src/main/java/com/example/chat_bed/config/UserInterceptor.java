package com.example.chat_bed.config;

import com.example.chat_bed.user.userdao;
import com.example.chat_bed.util.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private userdao dao;
    @Autowired
    private Jwt jwt;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      //  System.out.println("前置方法，正在执行");
        Map map = new HashMap();
        String token = request.getHeader("cookie");
        //String userid = request.getHeader("userid");
        String userid =(jwt.getClaimByName(token,"userid"));
        System.out.println(token);
        System.out.println(userid);
        if(userid.equals("error")){
            map.put("code",1002);
            map.put("msg", "没有权限");
            response.setContentType("application/json;charset=UTF-8");
            String json = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(json);
            return false;
        }
        String key =(jwt.getClaimByName(token,"key"));
        Integer userid1=Integer.valueOf(userid);
        if(key==null||!key.equals(dao.getkey(userid1))){
            map.put("code",1002);
            map.put("msg", "没有权限");
            response.setContentType("application/json;charset=UTF-8");
            String json = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(json);
            return false;
        }
         if(jwt.verifyToken(token,(userid))){
            return true;
        }
        else {
            map.put("1002", "没有权限");
            response.setContentType("application/json;charset=UTF-8");
            String json = new ObjectMapper().writeValueAsString(map);
            response.getWriter().println(json);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
     //   System.out.println("后置方法，正在执行");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
     //   System.out.println("完成方法，正在执行");
    }



}
