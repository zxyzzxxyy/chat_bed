package com.example.chat_bed.Aop;

import com.alibaba.fastjson.JSONObject;
import com.example.chat_bed.AMQP.send;
import com.example.chat_bed.Annotation.ExplanAnnotation;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Aspect
public class UserLoggerAspect {
    @Autowired
    private Ip2regionSearcher ip2regionSearcher;
    @Autowired
    private Aoplog logs;
    @Autowired
    private send send;

    /**
     * AOP 记录用户访问信息
     * @param joinpoint
     */
    @Around("execution(public * com.example.chat_bed.user.userservice.*(..))")
    public Object Around(ProceedingJoinPoint joinpoint) throws Throwable {
        String[] str = (Arrays.toString(joinpoint.getArgs())).substring(1,Arrays.toString(joinpoint.getArgs()).length()-1).split("}, \\{",2);
        System.out.println(Arrays.toString(joinpoint.getArgs()));
        //请求参数Map字符串
        str[0]=str[0]+"}";
        //请求头Map字符串
        str[1]="{"+str[1];
        //Object强转换为Map
        Map map = mapStringToMap(str[0]);
        map.putAll(mapStringToMap(str[1]));
        //Map强转为UserLogger
        UserLogger log = JSONObject.parseObject(JSONObject.toJSONString(map),UserLogger.class);
        //获取注解信息
        Signature signature = joinpoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            ExplanAnnotation annoObj= method.getAnnotation(ExplanAnnotation.class);
            log.setMethod_name(annoObj.name());
            log.setExplain(annoObj.explain());

        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.setVisit_time(formatter.format(System.currentTimeMillis()));
        HashMap returnmessage =(HashMap)joinpoint.proceed();
        log.setStatus(returnmessage.get("msg").toString());
        if(null!=returnmessage.get("token"))
            log.setToken(returnmessage.get("token").toString());
        if(null!=returnmessage.get("APItoken"))
            log.setApitoken(returnmessage.get("APItoken").toString());
        log.setAddress(ip2regionSearcher.getAddress(log.getIp()));
        if(log.getMethod_name().equals("login")){
            log.setUserid(logs.getuserid(log));
            log.setPhone(logs.getphone(log));
            if(log.getStatus().equals("登陆成功"))
            send.systemmsgsendsuccess(JSONObject.toJSONString(log));
            else  send.systemmsgsendwarn(JSONObject.toJSONString(log));
        }
        if(!log.getMethod_name().equals("getmyself")&&!log.getMethod_name().equals("getheself"))
        logs.newuserlog(log);
        return returnmessage;
    }
    public static Map<String,String> mapStringToMap(String str){
        str = str.substring(1, str.length()-1);
        String[] strs = str.split(",");
        Map<String,String> map = new HashMap<String, String>();
        for (String string : strs) {
            String key = string.split("=")[0];
            String value = string.split("=")[1];
            // 去掉头部空格
            String key1 = key.trim();
            String value1 = value.trim();
            map.put(key1, value1);
        }
        return map;
    }
}
